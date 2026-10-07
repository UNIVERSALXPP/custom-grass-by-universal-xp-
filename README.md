# Vibrant Grass Toggle (Fabric, Minecraft 1.21.11)

Vibrant Grass (3d3d tone) texture pack ko mod me bundle kiya hai, on/off toggle ke saath.
Pack ki files bilkul unchanged hain (`src/main/resources/resourcepacks/vibrant_grass`).

## Toggle kaise karein (3 tareeke)
1. **Keybind** - default `G` (Options > Controls > "Vibrant Grass" me change kar sakte ho)
2. **Chat command** - `/vibrantgrass` (toggle), `/vibrantgrass on`, `/vibrantgrass off`  (mobile ke liye best)
3. **Options > Resource Packs** - "Vibrant Grass" pack ko left/right move karo

Mod pehli baar install karne par pack by default ON hota hai.

## Requirements
Fabric Loader 0.18.1+, Fabric API, Java 21, Minecraft 1.21.11. Client-side only.

## GitHub se jar banana
1. Ye poora folder GitHub repo me upload karo
2. Actions tab > "Build mod" > latest run > Artifacts > `vibrant-grass-toggle-jar`
3. Zip ke andar `vibrant-grass-toggle-1.0.0.jar` ko mods folder me daalo

(Gradle wrapper jar included nahi hai; workflow khud Gradle 8.14.3 install karta hai.)

## Honest note
Ye code maine bina compile kiye likha hai (sandbox me internet nahi tha). Pack ki files verify ki hain,
lekin Java code ka pehla real test GitHub build hoga. Agar build error aaye to error text bhej dena, fix kar dunga.
