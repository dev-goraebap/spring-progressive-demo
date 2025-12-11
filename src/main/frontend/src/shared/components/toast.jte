<div x-data class="toast toast-end toast-middle z-[9999]">
    <div x-show="$store.toast.isOpen" x-transition class="alert alert-error z-[9999]">
        <span x-text="$store.toast.message"></span>
    </div>
</div>