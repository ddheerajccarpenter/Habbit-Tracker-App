#!/usr/bin/env python3
"""
Generate all app logos, launcher mipmaps, and web icons from the user uploaded asset.
"""
from PIL import Image, ImageDraw
import os

SRC_PATH = "/Users/dheerajcarpenter/.gemini/antigravity/brain/1f4501d4-a7d4-47ef-a9f8-fc95ab1e96aa/.user_uploaded/media_1795018235365_87c421fa.jpg"

def main():
    img = Image.open(SRC_PATH).convert("RGBA")
    
    # Bounding box & center
    # Emblem is centered around (511, 505), size ~308x306
    cx, cy = 511, 505
    crop_size = 460
    half = crop_size // 2
    crop_box = (cx - half, cy - half, cx + half, cy + half)
    cropped = img.crop(crop_box)
    
    # 1. Base 1024x1024 master
    master_1024 = cropped.resize((1024, 1024), Image.Resampling.LANCZOS)
    master_512 = cropped.resize((512, 512), Image.Resampling.LANCZOS)
    
    # Ensure directories exist
    os.makedirs("app/src/main/res/drawable", exist_ok=True)
    os.makedirs("app/src/main/res/drawable-nodpi", exist_ok=True)
    
    # Drawables
    master_512.save("app/src/main/res/drawable/app_logo.png", "PNG")
    master_1024.save("app/src/main/res/drawable/app_logo_full.png", "PNG")
    master_1024.save("app/src/main/res/drawable-nodpi/app_logo.png", "PNG")
    print("Saved Android drawables: app_logo.png, app_logo_full.png")

    # Mipmap densities
    densities = {
        "mipmap-mdpi": 48,
        "mipmap-hdpi": 72,
        "mipmap-xhdpi": 96,
        "mipmap-xxhdpi": 144,
        "mipmap-xxxhdpi": 192
    }

    for dir_name, size in densities.items():
        target_dir = os.path.join("app/src/main/res", dir_name)
        os.makedirs(target_dir, exist_ok=True)
        
        # Standard square/squircle icon
        resized = cropped.resize((size, size), Image.Resampling.LANCZOS)
        resized.save(os.path.join(target_dir, "ic_launcher.png"), "PNG")
        
        # Round icon (circular mask)
        round_icon = Image.new("RGBA", (size, size), (0, 0, 0, 0))
        mask = Image.new("L", (size, size), 0)
        draw = ImageDraw.Draw(mask)
        draw.ellipse((0, 0, size - 1, size - 1), fill=255)
        
        round_icon.paste(resized, (0, 0), mask)
        round_icon.save(os.path.join(target_dir, "ic_launcher_round.png"), "PNG")
        print(f"Saved {dir_name}: {size}x{size}")

    # Web & Dist icons
    for folder in ["web", "dist"]:
        if os.path.exists(folder):
            fav = cropped.resize((64, 64), Image.Resampling.LANCZOS)
            fav.save(os.path.join(folder, "favicon.png"), "PNG")
            app_icon = cropped.resize((512, 512), Image.Resampling.LANCZOS)
            app_icon.save(os.path.join(folder, "app-icon.png"), "PNG")
            print(f"Saved web icons in {folder}/")

if __name__ == "__main__":
    main()
