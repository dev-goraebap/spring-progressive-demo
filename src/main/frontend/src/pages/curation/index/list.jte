@import xyz.goraebap.spring_progressive_demo.infra.view_model.CurationViewModel
@import java.util.List

@param List<CurationViewModel> items

@for(var item: items)
    <div class="border-b border-base-300">
        <div class="p-4 flex flex-col items-start gap-2">
            <a href="${item.getLink()}" target="_blank" class="text-lg font-bold hover:text-primary">${item.getTitle()}</a>
            @if(item.getSnippet() != null)
                <p class="text-sm line-clamp-2">${item.getSnippet()}</p>
            @endif
            <div class="w-full flex items-center justify-between mt-2">
                <div class="text-sm text-base-content/70">${item.getSource()}</div>
                @if(item.getPubDate() != null)
                    <div class="text-sm">${item.getPubDate().toLocalDate().toString()}</div>
                @endif
            </div>
        </div>
    </div>
@endfor
