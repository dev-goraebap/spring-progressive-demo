@param String dominantColor
@param String url
@param String alt = "image"

<div x-data="{ loaded: false }"
     x-init="$nextTick(() => { if ($refs.img.complete) loaded = true })"
     class="w-full h-full flex items-center justify-center overflow-hidden transition-colors duration-700"
     style="background-color: ${dominantColor != null ? dominantColor : "#e5e7eb"}">
    <img x-ref="img"
         src="${url}"
         alt="${alt} thumbnail image"
         loading="lazy"
         @load="loaded = true"
         :class="loaded ? 'opacity-100' : 'opacity-0'"
         class="w-full h-full object-cover transition-opacity duration-500">
</div>