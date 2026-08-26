/*
 * AMD Patch for Voxy (1.21.1 fork)
 * Copyright (C) 2026 uricus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * Forked from 1MarcosDev/AMD-Patch-for-Voxy.
 */
package com.amdfix.mixin;

import me.cortex.voxy.client.core.gl.shader.ShaderLoader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Shared (loader-agnostic) AMD compatibility fix for Voxy's Hi-Z occlusion shader.
 *
 * Ported from 1MarcosDev/AMD-Patch-for-Voxy, moved into this mod's own package
 * (com.amdfix.mixin instead of me.cortex.voxy.client.mixin). On NeoForge, mods are
 * loaded as real Java modules, and the JPMS forbids two different modules from
 * exporting the same package ("split package") — since Voxy itself owns
 * me.cortex.voxy.client.mixin, this mixin can't live there too, even though it only
 * targets a Voxy class via @Mixin, it doesn't need to share Voxy's package.
 *
 * The target class (me.cortex.voxy.client.core.gl.shader.ShaderLoader) and its
 * `static String parse(String id)` signature are unchanged in the m3t4f1v3/voxy
 * "multiversion" 1.21.1 backport, on both Fabric and NeoForge, so this Mixin needs
 * no per-loader or per-version variants.
 */
@Mixin(ShaderLoader.class)
public class ShaderLoaderMixin {

    @Inject(method = "parse", at = @At("RETURN"), cancellable = true)
    private static void injectAmdFix(String id, CallbackInfoReturnable<String> cir) {
        String source = cir.getReturnValue();

        // Check if the shader contains the code we want to patch
        // The target code is: float sp = texelFetch(hizDepthSampler, ivec2(x, y), ml).r;
        if (source != null && source.contains("float sp = texelFetch(hizDepthSampler, ivec2(x, y), ml).r;")) {
            String fix = """
                float sp = texelFetch(hizDepthSampler, ivec2(x, y), ml).r;
                if (sp <= 0.0001f) {
                    sp = 1.0f;
                }
                """;
            String patched = source.replace("float sp = texelFetch(hizDepthSampler, ivec2(x, y), ml).r;", fix);
            cir.setReturnValue(patched);
        }
    }
}
