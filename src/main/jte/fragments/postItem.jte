@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel

@param PostViewModel post

<li class="flex flex-col gap-3 items-start p-4 border-b-4 border-base-300">
    @if(post.getThumbnailUrl() != null)
        <div x-data="{ loaded: false }"
             x-init="$nextTick(() => { if ($refs.img.complete) loaded = true })"
             class="w-full h-[170px] flex items-center justify-center overflow-hidden transition-colors duration-700 rounded-md"
             style="background-color: ${post.getThumbnailDominantColor() != null ? post.getThumbnailDominantColor() : "#e5e7eb"}">
            <img x-ref="img"
                 src="${post.getThumbnailUrl()}"
                 alt="${post.getTitle()} thumbnail image"
                 loading="lazy"
                 @load="loaded = true"
                 :class="loaded ? 'opacity-100' : 'opacity-0'"
                 class="w-full h-full object-cover transition-opacity duration-500">
        </div>
    @endif
    <h2 class="text-xl font-bold">${post.getTitle()}</h2>
    <p class="text-sm">${post.getSummary()}</p>
    <button class="font-bold">Read more</button>
</li>
