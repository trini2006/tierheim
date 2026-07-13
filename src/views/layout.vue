<template>
  <!-- Der Hauptcontainer wird untereinander (flex-col) statt nebeneinander ausgerichtet -->
  <div class="flex flex-col min-h-screen bg-gray-50">
    <!-- ========================================================= -->
    <!-- DESKTOP TOP-NAVIGATION (Wird NUR auf großen Bildschirmen oben angezeigt) -->
    <!-- ========================================================= -->
    <nav
      class="hidden lg:flex items-center justify-between w-full bg-[#536B53] text-white px-8 py-4 border-b border-gray-700 sticky top-0 z-50 shadow-md"
    >
      <!-- Logo / Name links -->
      <div class="flex items-center gap-3">
        <span class="text-2xl">🐾</span>
        <h1 class="text-xl font-bold">Tierheim Weiden</h1>
      </div>

      <!-- Navigations-Links rechts -->
      <div class="flex gap-4">
        <RouterLink
          to="/"
          class="px-4 py-2 rounded-lg hover:bg-slate-700 transition"
          exact-active-class="bg-green-600"
        >
          🏠 Startseite
        </RouterLink>
        <RouterLink
          to="/tierheim"
          class="px-4 py-2 rounded-lg hover:bg-slate-700 transition"
          active-class="bg-green-600"
        >
          🐾 Tierheim
        </RouterLink>
        <RouterLink
          to="/einstellungen"
          class="px-4 py-2 rounded-lg hover:bg-slate-700 transition"
          active-class="bg-green-600"
        >
          ⚙️ Einstellungen
        </RouterLink>
      </div>
    </nav>

    <!-- ========================================================= -->
    <!-- DER DYNAMISCHE INHALT (Für alle Bildschirmgrößen) -->
    <!-- ========================================================= -->
    <!-- max-w-7xl sorgt dafür, dass der Inhalt auf Riesen-Monitoren schön zentriert bleibt und nicht links klebt -->
    <main class="flex-grow p-4 md:p-8 max-w-7xl mx-auto w-full pb-24 lg:pb-8">
      <RouterView />
    </main>

    <!-- ========================================================= -->
    <!-- MOBILE BOTTOM-BAR (Wird ab Desktopgröße 'lg:' versteckt) -->
    <!-- ========================================================= -->
    <nav
      class="lg:hidden fixed bottom-0 left-0 right-0 h-16 bg-white border-t border-gray-200 flex justify-around items-center z-50 shadow-lg"
    >
      <RouterLink
        to="/"
        class="flex flex-col items-center text-gray-600"
        exact-active-class="text-green-600"
      >
        <span class="text-xl">🏠</span>
        <span class="text-xs">Start</span>
      </RouterLink>

      <RouterLink
        to="/tierheim"
        class="flex flex-col items-center text-gray-600"
        active-class="text-green-600"
      >
        <span class="text-xl">🐾</span>
        <span class="text-xs">Tierheim</span>
      </RouterLink>

      <RouterLink
        to="/einstellungen"
        class="flex flex-col items-center text-gray-600"
        active-class="text-green-600"
      >
        <span class="text-xl">⚙️</span>
        <span class="text-xs">Einstellungen</span>
      </RouterLink>
    </nav>
  </div>
</template>
