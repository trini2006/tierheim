<template>
  <div class="p-6 text-center">
    <div
      class="w-16 h-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mx-auto mb-4 text-2xl"
    >
      ✓
    </div>
    <h1 class="text-2xl font-bold text-gray-800">Erfolgreich reserviert!</h1>
    <p class="text-gray-600 mt-2">
      Dein Termin wurde für dich eingetragen. Wir freuen uns auf dich im Tierheim Weiden!
    </p>

    <RouterLink
      to="/"
      class="mt-6 inline-block px-4 py-2 bg-green-600 text-white rounded-xl shadow-md font-medium"
    >
      Zurück zur Startseite
    </RouterLink>
  </div>
</template>
