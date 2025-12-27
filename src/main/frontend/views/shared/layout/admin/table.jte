@param gg.jte.Content head
@param gg.jte.Content body
@param String emptyMessage = "데이터가 없습니다."
@param boolean isEmpty = false

<div class="flex-1 overflow-x-auto">
    <table class="table table-sm lg:table-md whitespace-nowrap">
        <thead>
        <tr class="border-b border-base-300">
            ${head}
        </tr>
        </thead>
        <tbody>
        @if(isEmpty)
            <tr>
                <td colspan="100" class="text-center py-12 text-base-content/60">
                    ${emptyMessage}
                </td>
            </tr>
        @else
            ${body}
        @endif
        </tbody>
    </table>
</div>
