<div x-data>
    <div
            x-show="$store.modal.isOpen === true"
            x-init="$watch('$store.modal.isOpen', () => $store.modal.init())"
            class="fixed inset-0 bg-base-100/80 flex items-center justify-center z-[9999]">
        <div class="w-full h-full sm:max-w-[20rem] bg-base-300 p-10 sm:h-auto sm:rounded-xl">
            <div id="modal-content">
                <div class="loading"></div>
            </div>
        </div>
    </div>
</div>
