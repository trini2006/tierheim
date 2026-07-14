<template>
  <div>
    <!-- Die salbeigrüne Hundekarte -->
    <div class="bg-[#D3DDD1] p-5 rounded-2xl shadow-sm border border-gray-200 relative">
      <div class="flex gap-4">
        <!-- Hundebild -->
        <div class="w-28 h-28 rounded-xl overflow-hidden bg-gray-300 flex-shrink-0 shadow-inner">
          <img
            src="https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=300&auto=format&fit=crop"
            alt="Dark"
            class="w-full h-full object-cover"
          />
        </div>

        <!-- Hundedetails -->
        <div class="flex-grow space-y-2 text-left relative">
          <!-- Orangefarbener Statuspunkt -->
          <span class="absolute top-0 right-0 w-3 h-3 bg-orange-500 rounded-full"></span>

          <h3 class="text-xl font-bold text-gray-900 leading-tight">Dark</h3>
          <p class="text-xs text-gray-600">Mischling<br />7 Jahre, Rüde</p>

          <!-- Charaktereigenschaften -->
          <div class="flex flex-wrap gap-1.5 pt-1">
            <span class="bg-white/80 px-2 py-0.5 rounded-full text-[11px] text-gray-700 font-medium"
              >ruhig</span
            >
            <span class="bg-white/80 px-2 py-0.5 rounded-full text-[11px] text-gray-700 font-medium"
              >zurückhaltend</span
            >
          </div>
        </div>
      </div>
    </div>

    <!-- Termindaten -->
    <div class="mt-6 space-y-2 text-left px-2">
      <div class="flex text-sm">
        <span class="font-bold w-20 text-gray-700">Datum:</span>
        <span class="text-gray-900">Mittwoch, 01.07.</span>
      </div>
      <div class="flex text-sm">
        <span class="font-bold w-20 text-gray-700">Uhrzeit:</span>
        <span class="text-gray-900">10:00 - 11:00 Uhr</span>
      </div>
    </div>

    <!-- Stornieren Button -->
    <div class="mt-6 text-left px-2">
      <button
        class="bg-red-400 hover:bg-red-500 text-gray-900 font-bold px-6 py-2.5 rounded-full shadow-sm transition-colors text-sm"
      >
        Termin stornieren
      </button>
    </div>
  </div>
</template>
