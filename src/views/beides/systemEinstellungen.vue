<template>
  <div class="max-w-md mx-auto px-6 py-4 text-left text-gray-800">
    <!-- Sektion 1: Persönliches -->
    <div class="mb-8">
      <div class="flex items-center justify-between mb-2">
        <div class="flex items-center gap-3">
          <!-- Person Icon -->
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="w-6 h-6 text-gray-900"
          >
            <path
              fill-rule="evenodd"
              d="M7.5 6a4.5 4.5 0 1 1 9 0 4.5 4.5 0 0 1-9 0ZM3.751 20.105a8.25 8.25 0 0 1 16.498 0 .75.75 0 0 1-.437.695A18.683 18.683 0 0 1 12 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 0 1-.437-.695Z"
              clip-rule="evenodd"
            />
          </svg>
          <h2 class="font-bold text-base">Persönliches</h2>
        </div>
        <!-- Oranger Statuspunkt aus der Vorlage -->
        <span class="w-4 h-4 rounded-full bg-orange-500 block mr-2"></span>
      </div>
      <p class="text-xs text-gray-500 pl-9 font-medium">Mitgliednummer: 1234567890</p>
    </div>

    <!-- Sektion 2: Allgemein -->
    <div class="mb-8">
      <div class="flex items-center gap-3 mb-4">
        <!-- Schraubenschlüssel Icon -->
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          fill="currentColor"
          class="w-6 h-6 text-gray-900"
        >
          <path
            fill-rule="evenodd"
            d="M12 2.25a.75.75 0 0 1 .612.317l1.5 2.25A.75.75 0 0 1 13.5 6h-3a.75.75 0 0 1-.612-1.183l1.5-2.25A.75.75 0 0 1 12 2.25ZM3 10.5a.75.75 0 0 1 .75-.75h16.5a.75.75 0 0 1 0 1.5H3.75A.75.75 0 0 1 3 10.5Zm1.144 4.856a.75.75 0 0 1 1.06 0L12 22.141l6.796-6.785a.75.75 0 1 1 1.06 1.06l-7.325 7.315a.75.75 0 0 1-1.06 0l-7.326-7.315a.75.75 0 0 1 0-1.06Z"
            clip-rule="evenodd"
          />
          <path
            d="M11.25 6V9.75H3.75V6h7.5ZM20.25 6V9.75h-7.5V6h7.5ZM11.25 11.25v3.42l-5.32-5.32h5.32ZM12.75 11.25h5.32l-5.32 5.32v-3.42Z"
          />
          <path
            d="M6.547 5.303a.75.75 0 1 1 1.06-1.06l2.25 2.25a.75.75 0 0 1 0 1.06l-2.25 2.25a.75.75 0 1 1-1.06-1.06L8.188 7 6.547 5.303ZM17.453 5.303a.75.75 0 1 0-1.06-1.06l-2.25 2.25a.75.75 0 0 0 0 1.06l2.25 2.25a.75.75 0 1 0 1.06-1.06L15.812 7l1.641-1.697Z"
          />
        </svg>
        <h2 class="font-bold text-base">Allgemein</h2>
      </div>

      <div class="flex items-center justify-between pl-9">
        <span class="text-xs font-medium text-gray-700">Schriftgröße</span>
        <div class="flex gap-2 bg-gray-100 p-0.5 rounded-xl">
          <button
            @click="settings.fontSize = 'normal'"
            :class="[
              'text-xs font-semibold px-4 py-1.5 rounded-lg transition-all',
              settings.fontSize === 'normal'
                ? 'bg-[#536B53] text-white shadow-sm'
                : 'text-gray-600 hover:text-gray-900',
            ]"
          >
            Normal
          </button>
          <button
            @click="settings.fontSize = 'large'"
            :class="[
              'text-xs font-semibold px-4 py-1.5 rounded-lg transition-all',
              settings.fontSize === 'large'
                ? 'bg-[#536B53] text-white shadow-sm'
                : 'text-gray-600 hover:text-gray-900',
            ]"
          >
            Groß
          </button>
        </div>
      </div>
    </div>

    <!-- Sektion 3: Benachrichtigungen -->
    <div class="mb-8">
      <div class="flex items-center gap-3 mb-4">
        <!-- Glocken Icon -->
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          fill="currentColor"
          class="w-6 h-6 text-gray-900"
        >
          <path
            fill-rule="evenodd"
            d="M5.25 9a6.75 6.75 0 0 1 13.5 0v.75c0 2.123.8 4.057 2.118 5.52a.75.75 0 0 1-.297 1.206c-1.544.57-3.16.99-4.831 1.243a3.75 3.75 0 1 1-7.48 0 24.585 24.585 0 0 1-4.831-1.244.75.75 0 0 1-.298-1.205A11.04 11.04 0 0 0 5.25 9.75V9Zm4.502 8.9a2.25 2.25 0 1 0 4.496 0 25.057 25.057 0 0 1-4.496 0Z"
            clip-rule="evenodd"
          />
        </svg>
        <h2 class="font-bold text-base">Benachrichtigungen</h2>
      </div>

      <div class="space-y-4 pl-9">
        <!-- Nachrichtenton -->
        <div class="flex items-center justify-between">
          <span class="text-xs font-medium text-gray-700">Nachrichtenton</span>
          <div class="flex gap-2 bg-gray-100 p-0.5 rounded-xl">
            <button
              @click="settings.sound = true"
              :class="[
                'text-xs font-semibold px-4 py-1.5 rounded-lg transition-all',
                settings.sound
                  ? 'bg-[#536B53] text-white shadow-sm'
                  : 'text-gray-600 hover:text-gray-900',
              ]"
            >
              Ja
            </button>
            <button
              @click="settings.sound = false"
              :class="[
                'text-xs font-semibold px-4 py-1.5 rounded-lg transition-all',
                !settings.sound
                  ? 'bg-[#536B53] text-white shadow-sm'
                  : 'text-gray-600 hover:text-gray-900',
              ]"
            >
              Nein
            </button>
          </div>
        </div>

        <!-- Erinnerung -->
        <div class="flex items-center justify-between">
          <span class="text-xs font-medium text-gray-700">Erinnerung</span>
          <div class="flex gap-2 bg-gray-100 p-0.5 rounded-xl">
            <button
              @click="settings.reminder = true"
              :class="[
                'text-xs font-semibold px-4 py-1.5 rounded-lg transition-all',
                settings.reminder
                  ? 'bg-[#536B53] text-white shadow-sm'
                  : 'text-gray-600 hover:text-gray-900',
              ]"
            >
              Ja
            </button>
            <button
              @click="settings.reminder = false"
              :class="[
                'text-xs font-semibold px-4 py-1.5 rounded-lg transition-all',
                !settings.reminder
                  ? 'bg-[#536B53] text-white shadow-sm'
                  : 'text-gray-600 hover:text-gray-900',
              ]"
            >
              Nein
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Sektion 4: App-Sprache -->
    <div class="mb-12 relative">
      <div class="flex items-center gap-3 mb-3">
        <!-- Weltkugel Icon -->
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          fill="currentColor"
          class="w-6 h-6 text-gray-900"
        >
          <path
            fill-rule="evenodd"
            d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25ZM12.75 6a.75.75 0 0 0-1.5 0v.75H9.75a.75.75 0 0 0 0 1.5h1.5v2.25H9a.75.75 0 0 0 0 1.5h2.25V15h-1.5a.75.75 0 0 0 0 1.5h1.5v1.5a.75.75 0 0 0 1.5 0v-1.5h1.5a.75.75 0 0 0 0-1.5h-1.5v-2.25H15a.75.75 0 0 0 0-1.5h-2.25V8.25h1.5a.75.75 0 0 0 0-1.5h-1.5V6Z"
            clip-rule="evenodd"
          />
        </svg>
        <h2 class="font-bold text-base">App-Sprache</h2>
      </div>

      <!-- Klickbares Sprachfeld (Dropdown Trigger) -->
      <div class="pl-9">
        <button
          @click="isDropdownOpen = !isDropdownOpen"
          class="flex items-center justify-between w-full max-w-[200px] border border-gray-200 rounded-xl px-3 py-2 bg-white hover:bg-gray-50 text-xs font-semibold shadow-sm transition-all text-left"
        >
          <span>{{ currentLanguageLabel }}</span>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="2.5"
            stroke="currentColor"
            :class="[
              'w-3 h-3 text-gray-500 transition-transform',
              isDropdownOpen ? 'rotate-180' : '',
            ]"
          >
            <path stroke-linecap="round" stroke-linejoin="round" d="m19.5 8.25-7.5 7.5-7.5-7.5" />
          </svg>
        </button>

        <!-- Ausklappbares Sprachen-Menü -->
        <div
          v-if="isDropdownOpen"
          class="absolute left-9 mt-1 w-full max-w-[200px] bg-white border border-gray-200 rounded-xl shadow-xl z-50 overflow-hidden max-h-48 overflow-y-auto transition-all"
        >
          <button
            v-for="lang in availableLanguages"
            :key="lang.code"
            @click="selectLanguage(lang)"
            :class="[
              'w-full text-left px-4 py-2.5 text-xs font-medium transition-colors border-b border-gray-50 last:border-none',
              settings.language === lang.code
                ? 'bg-[#536B53]/10 text-[#536B53] font-bold'
                : 'text-gray-700 hover:bg-gray-50',
            ]"
          >
            {{ lang.label }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// Zentraler Einstellungs-State
const settings = ref({
  fontSize: 'normal', // 'normal' oder 'large'
  sound: true, // true = Ja, false = Nein
  reminder: true, // true = Ja, false = Nein
  language: 'de', // Kürzel der gewählten Sprache
})

// Dropdown-Steuerung
const isDropdownOpen = ref(false)

// Eine feine Auswahl der gängigsten europäischen Sprachen
const availableLanguages = [
  { code: 'de', label: 'Deutsch' },
  { code: 'en', label: 'English' },
]

// Berechnet den Namen der aktuell gewählten Sprache
const currentLanguageLabel = computed(() => {
  const found = availableLanguages.find((l) => l.code === settings.value.language)
  return found ? found.label : 'Sprache wählen'
})

// Sprache setzen und Dropdown schließen
const selectLanguage = (lang) => {
  settings.value.language = lang.code
  isDropdownOpen.value = false
}
</script>
