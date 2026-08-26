# <img src="versions/fabric/src/main/resources/assets/amd_patch_for_voxy/icon.png" width="64" valign="middle" alt="Logo"> AMD Patch for Voxy (Unofficial)

**An open-source, standalone patcher to fix rendering issues with the Voxy mod on AMD GPUs.**

![License](https://img.shields.io/badge/License-GPLv3-blue.svg) ![Status](https://img.shields.io/badge/Status-Unofficial-red)

---

## Important Disclaimer

**This is an UNOFFICIAL patch.**

* This project is **NOT** associated with, endorsed by, supported by, or affiliated with MCRcortex (the creator of Voxy) in any way.
* **Do NOT report bugs** encountered while using this patch to the official Voxy issue trackers or Discord.
* This software is provided "as-is" to enable interoperability for AMD users who cannot currently use the mod.

---

## What is this?

Voxy is an incredible LOD mod, but it currently suffers from critical compatibility issues with AMD Radeon drivers, often resulting in invisible terrain, rendering glitches, or crashes.

This is a **standalone patcher** that uses Mixins to modify rendering behavior at runtime. It aims to make the mod playable on AMD hardware by addressing specific occlusion and rendering conflicts.

This fork adds **NeoForge support alongside Fabric**, targeting the [voxy backport](https://github.com/m3t4f1v3/voxy/tree/multiversion) for Minecraft 1.21.1.

### The Technical Problem

Voxy uses a **Hierarchical Z-Buffer (HiZ)** compute shader to cull invisible LOD chunks. On AMD GPUs the driver returns bogus near-zero values for `texelFetch` on depth textures. This corrupts the reduction (min/max) operation, causing chunks to flicker or disappear as the camera moves.

### Our Fix

This patch intercepts the HiZ traversal compute-shader source before Voxy compiles it, and injects a clamp that forces buggy near-zero depth reads to `1.0f` (far plane). This makes the culling more conservative (render rather than incorrectly cull).

## How it Works (Technical & Legal Note)

These `.jar` files contain **NO original code, binaries, or assets** from Voxy.

They are strictly patchers that utilize the Mixin system (Fabric and NeoForge respectively) to inject the fix into the game's memory as it launches. Because of this, **you must have the [voxy backport](https://github.com/m3t4f1v3/voxy/tree/multiversion) installed** for this patch to work — official Voxy doesn't support 1.21.1, so the backport is the only option this fork targets. This patch does not function on its own.

## Requirements

- **Minecraft:** 1.21.1
- **Loader:** Fabric Loader >= 0.16.9 **or** NeoForge >= 21.1.230
- **Java:** 21
- **[Voxy Backport](https://github.com/m3t4f1v3/voxy/tree/multiversion):** >= 0.2.15-beta

## Tested Hardware

The community has reported successful tests on the following GPU architectures:

* **RX 500 Series:** (e.g., RX 550, RX 560, RX 580) — *Confirmed Working*

> *Have you tested this on RX 6000, 7000, or older cards? Please open an Issue to let us know so we can update this list!*

## Installation

1. Ensure you have installed the appropriate mod loader (Fabric **or** NeoForge) for Minecraft 1.21.1.
2. Download and install the [voxy backport](https://github.com/m3t4f1v3/voxy/tree/multiversion) build matching your loader.
3. Download this patch's release matching your loader from the [Releases page](../../releases) of this repository.
4. Place **both** the Voxy jar AND this patch jar into your `.minecraft/mods` folder.
5. Launch the game.

## Building from Source

Requires JDK 21. The voxy backport is not published to any Maven repo, so its jars
must be built from [source](https://github.com/m3t4f1v3/voxy/tree/multiversion) (or otherwise
obtained) and placed manually into `versions/fabric/libs/` and `versions/neoforge/libs/` before
building — the build will fail with a clear error if they're missing.

To build a jar for **only one loader**, run just that subproject's task — you don't need to
provide a Voxy jar for the loader you're not building. `org.gradle.configureondemand=true` in
`gradle.properties` keeps Gradle from configuring (and triggering Loom's Minecraft remapping for)
the other loader's subproject when you only asked for one. This is a Gradle *incubating* feature —
stable enough for this project's simple two-subproject layout, but if you ever see stale/odd
behavior after adding project interdependencies, try removing that line first.

```bash
./gradlew :fabric:build     # Fabric only   -> versions/fabric/build/libs/
./gradlew :neoforge:build   # NeoForge only -> versions/neoforge/build/libs/
./gradlew build             # both loaders
```

## Contributing

This project is **Open Source**. Unlike the original mod, we believe in community collaboration.
If you know how to improve performance or fix other bugs, **Pull Requests are welcome!**

## License

This project is licensed under the **GNU General Public License v3.0 (GPLv3)**.

**What this means:**
* You are free to use, copy, modify, and distribute this software.
* **If you modify and distribute this patch, you MUST keep it Open Source** under the same license.
* You must preserve the author's copyright and credits.

> **License Note:**
> The GPLv3 license applies **ONLY** to the source code within this repository (the Mixins/Patch). It does **NOT** extend to the original Voxy mod, which remains the proprietary intellectual property of MCRcortex and is subject to its own "All Rights Reserved" license, nor to the voxy backport.

## Credits

* **This Fork Author:** [uricus](https://github.com/uricus/AMD-Patch-for-Voxy)
* **Original Patch Author:** [Marquinhos0550](https://github.com/1MarcosDev/AMD-Patch-for-Voxy)
* **Voxy Backport:** [m3t4f1v3](https://github.com/m3t4f1v3/voxy/tree/multiversion)
* **Voxy Original Creator:** [MCRcortex](https://github.com/MCRcortex/voxy) (All rights to the original Voxy mod belong to him).

---
*Run with freedom.*
