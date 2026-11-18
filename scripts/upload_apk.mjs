import { createClient } from '@supabase/supabase-js';
import fs from 'fs';
import path from 'path';

const supabaseUrl = process.env.SUPABASE_URL || 'https://mtlckvkyimoqbrlvirch.supabase.co';
const supabaseKey = process.env.SUPABASE_SERVICE_ROLE_KEY;

if (!supabaseKey) {
  console.error("Missing SUPABASE_SERVICE_ROLE_KEY");
  process.exit(1);
}

const supabase = createClient(supabaseUrl, supabaseKey);

async function uploadApk(fileName, remotePath, maxRetries = 3) {
  const filePath = path.resolve(process.cwd(), fileName);
  if (!fs.existsSync(filePath)) {
    console.error(`File ${filePath} does not exist`);
    return;
  }
  const fileBuffer = fs.readFileSync(filePath);

  for (let attempt = 1; attempt <= maxRetries; attempt++) {
    try {
      console.log(`Uploading ${fileName} (${fileBuffer.length} bytes) to apk/${remotePath} (attempt ${attempt}/${maxRetries})...`);
      const { data, error } = await supabase.storage
        .from('apk')
        .upload(remotePath, fileBuffer, {
          contentType: 'application/vnd.android.package-archive',
          upsert: true
        });

      if (error) {
        throw error;
      }
      console.log(`Successfully uploaded ${remotePath}:`, data);
      return;
    } catch (err) {
      console.error(`Attempt ${attempt} failed for ${remotePath}:`, err.message || err);
      if (attempt === maxRetries) {
        throw err;
      }
      const waitTime = attempt * 2000;
      console.log(`Waiting ${waitTime}ms before retry...`);
      await new Promise(r => setTimeout(r, waitTime));
    }
  }
}

async function main() {
  const pkg = JSON.parse(fs.readFileSync('./package.json', 'utf-8'));
  const versionedName = `TDLxHT.${pkg.version}.apk`;
  await uploadApk(versionedName, versionedName);
  await new Promise(r => setTimeout(r, 2000));
  await uploadApk('TDlxHT.apk', 'TDlxHT.apk');
}

main().catch(err => {
  console.error(err);
  process.exit(1);
});

