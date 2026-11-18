#!/usr/bin/env node
import fs from 'fs';
import path from 'path';

const rootDir = process.cwd();

// 1. Read app/build.gradle.kts
const gradlePath = path.join(rootDir, 'app', 'build.gradle.kts');
let gradleContent = fs.readFileSync(gradlePath, 'utf-8');

const codeMatch = gradleContent.match(/versionCode\s*=\s*(\d+)/);
const nameMatch = gradleContent.match(/versionName\s*=\s*"([^"]+)"/);

if (!codeMatch || !nameMatch) {
  console.error("Could not find versionCode or versionName in app/build.gradle.kts");
  process.exit(1);
}

const currentCode = parseInt(codeMatch[1], 10);
const currentName = nameMatch[1];

// Parse semver: major.minor.patch
const parts = currentName.split('.').map(p => parseInt(p, 10));
let major = parts[0] || 3;
let minor = parts[1] || 0;
let patch = parts[2] || 0;

// Rule: Next +1 in minor to major
// Increment minor; if minor >= 10, roll over to major
minor += 1;
if (minor >= 10) {
  major += 1;
  minor = 0;
}
patch = 0;

const nextCode = currentCode + 1;
const nextName = `${major}.${minor}.${patch}`;

console.log(`Bumping version: ${currentName} (code ${currentCode}) -> ${nextName} (code ${nextCode})`);

// 2. Update app/build.gradle.kts
gradleContent = gradleContent.replace(/versionCode\s*=\s*\d+/, `versionCode = ${nextCode}`);
gradleContent = gradleContent.replace(/versionName\s*=\s*"[^"]+"/, `versionName = "${nextName}"`);
fs.writeFileSync(gradlePath, gradleContent, 'utf-8');
console.log(`✓ Updated app/build.gradle.kts`);

// 3. Update package.json
const pkgPath = path.join(rootDir, 'package.json');
if (fs.existsSync(pkgPath)) {
  const pkg = JSON.parse(fs.readFileSync(pkgPath, 'utf-8'));
  pkg.version = nextName;
  fs.writeFileSync(pkgPath, JSON.stringify(pkg, null, 2) + '\n', 'utf-8');
  console.log(`✓ Updated package.json`);
}

// 4. Update version.json & web/version.json
const targetFileName = `TDLxHT.${nextName}.apk`;
const today = new Date().toISOString().split('T')[0];

const versionJsonContent = {
  version: nextName,
  versionCode: nextCode,
  filename: targetFileName,
  sha256: "",
  apkUrl: `https://mtlckvkyimoqbrlvirch.supabase.co/storage/v1/object/public/apk/${targetFileName}`,
  fallbackUrl: `https://github.com/ddheerajccarpenter/TDLxHT/releases/download/v${nextName}/${targetFileName}`,
  githubReleaseUrl: `https://github.com/ddheerajccarpenter/TDLxHT/releases/tag/v${nextName}`,
  releaseDate: today
};

const vJsonStr = JSON.stringify(versionJsonContent, null, 2) + '\n';
fs.writeFileSync(path.join(rootDir, 'version.json'), vJsonStr, 'utf-8');
const webVersionPath = path.join(rootDir, 'web', 'version.json');
if (fs.existsSync(path.dirname(webVersionPath))) {
  fs.writeFileSync(webVersionPath, vJsonStr, 'utf-8');
}
console.log(`✓ Updated version.json and web/version.json`);

// 5. Update web/index.html fallback values if present
const indexPath = path.join(rootDir, 'web', 'index.html');
if (fs.existsSync(indexPath)) {
  let indexHtml = fs.readFileSync(indexPath, 'utf-8');
  indexHtml = indexHtml.replace(/DEFAULT_RELEASE\s*=\s*\{\s*versionName:\s*"[^"]+",\s*versionCode:\s*\d+\s*\}/,
    `DEFAULT_RELEASE = { versionName: "${nextName}", versionCode: ${nextCode} }`);
  fs.writeFileSync(indexPath, indexHtml, 'utf-8');
  console.log(`✓ Updated web/index.html DEFAULT_RELEASE`);
}

console.log(`Version successfully bumped to ${nextName} (code ${nextCode})!`);
