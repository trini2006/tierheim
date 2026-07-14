<template>
  <div
    class="w-full min-h-screen flex flex-col items-center justify-start px-6 py-8 text-left text-gray-800"
  >
    <div class="w-full max-w-md md:max-w-lg lg:max-w-xl">
      <!-- Sektion 1: Persönliches -->
      <div class="mb-10">
        <div class="flex items-center gap-3 mb-2">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="w-6 h-6 text-gray-900 md:w-7 md:h-7"
          >
            <path
              fill-rule="evenodd"
              d="M7.5 6a4.5 4.5 0 1 1 9 0 4.5 4.5 0 0 1-9 0ZM3.751 20.105a8.25 8.25 0 0 1 16.498 0 .75.75 0 0 1-.437.695A18.683 18.683 0 0 1 12 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 0 1-.437-.695Z"
              clip-rule="evenodd"
            />
          </svg>
          <h2 class="font-bold text-base md:text-lg lg:text-xl">Persönliches</h2>
        </div>

        <div class="pl-9 flex items-center gap-4">
          <p class="text-xs md:text-sm lg:text-base text-gray-500 font-medium">
            Mitgliednummer: 1234567890
          </p>
          <span class="w-4 h-4 rounded-full bg-orange-500 block"></span>
        </div>
      </div>

      <!-- Sektion 2: Allgemein -->
      <div class="mb-10">
        <div class="flex items-center gap-3 mb-4">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="w-6 h-6 text-gray-900 md:w-7 md:h-7"
          >
            <path
              fill-rule="evenodd"
              d="M12 2.25a.75.75 0 0 1 .612.317l1.5 2.25A.75.75 0 0 1 13.5 6h-3a.75.75 0 0 1-.612-1.183l1.5-2.25A.75.75 0 0 1 12 2.25ZM3 10.5a.75.75 0 0 1 .75-.75h16.5a.75.75 0 0 1 0 1.5H3.75A.75.75 0 0 1 3 10.5Zm1.144 4.856a.75.75 0 0 1 1.06 0L12 22.141l6.796-6.785a.75.75 0 1 1 1.06 1.06l-7.325 7.315a.75.75 0 0 1-1.06 0l-7.326-7.315a.75.75 0 0 1 0-1.06Z"
              clip-rule="evenodd"
            />
          </svg>
          <h2 class="font-bold text-base md:text-lg lg:text-xl">Allgemein</h2>
        </div>

        <div class="flex items-center gap-12 pl-9">
          <span class="text-xs md:text-sm lg:text-base font-medium text-gray-700 w-24 md:w-32"
            >Schriftgröße</span
          >
          <div class="flex gap-2 bg-gray-100 p-0.5 rounded-xl">
            <!-- "settings." gelöscht, greift nun direkt auf fontSize zu -->
            <button
              @click="fontSize = 'normal'"
              :class="[
                'text-xs md:text-sm font-semibold px-4 py-1.5 rounded-lg transition-all',
                fontSize === 'normal'
                  ? 'bg-[#536B53] text-white shadow-sm'
                  : 'text-gray-600 hover:text-gray-900',
              ]"
            >
              Normal
            </button>
            <button
              @click="fontSize = 'large'"
              :class="[
                'text-xs md:text-sm font-semibold px-4 py-1.5 rounded-lg transition-all',
                fontSize === 'large'
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
      <div class="mb-10">
        <div class="flex items-center gap-3 mb-4">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="w-6 h-6 text-gray-900 md:w-7 md:h-7"
          >
            <path
              fill-rule="evenodd"
              d="M5.25 9a6.75 6.75 0 0 1 13.5 0v.75c0 2.123.8 4.057 2.118 5.52a.75.75 0 0 1-.297 1.206c-1.544.57-3.16.99-4.831 1.243a3.75 3.75 0 1 1-7.48 0 24.585 24.585 0 0 1-4.831-1.244.75.75 0 0 1-.298-1.205A11.04 11.04 0 0 0 5.25 9.75V9Zm4.502 8.9a2.25 2.25 0 1 0 4.496 0 25.057 25.057 0 0 1-4.496 0Z"
              clip-rule="evenodd"
            />
          </svg>
          <h2 class="font-bold text-base md:text-lg lg:text-xl">Benachrichtigungen</h2>
        </div>

        <div class="space-y-4 pl-9">
          <!-- Pop-Ups -->
          <div class="flex items-center gap-12">
            <span class="text-xs md:text-sm lg:text-base font-medium text-gray-700 w-24 md:w-32"
              >Pop-Ups</span
            >
            <div class="flex gap-2 bg-gray-100 p-0.5 rounded-xl">
              <!-- Direktzugriff auf popups -->
              <button
                @click="popups = true"
                :class="[
                  'text-xs md:text-sm font-semibold px-4 py-1.5 rounded-lg transition-all',
                  popups
                    ? 'bg-[#536B53] text-white shadow-sm'
                    : 'text-gray-600 hover:text-gray-900',
                ]"
              >
                Ja
              </button>
              <button
                @click="popups = false"
                :class="[
                  'text-xs md:text-sm font-semibold px-4 py-1.5 rounded-lg transition-all',
                  !popups
                    ? 'bg-[#536B53] text-white shadow-sm'
                    : 'text-gray-600 hover:text-gray-900',
                ]"
              >
                Nein
              </button>
            </div>
          </div>

          <!-- Erinnerung -->
          <div class="flex items-center gap-12">
            <span class="text-xs md:text-sm lg:text-base font-medium text-gray-700 w-24 md:w-32"
              >Erinnerung</span
            >
            <div class="flex gap-2 bg-gray-100 p-0.5 rounded-xl">
              <!-- Direktzugriff auf reminder -->
              <button
                @click="reminder = true"
                :class="[
                  'text-xs md:text-sm font-semibold px-4 py-1.5 rounded-lg transition-all',
                  reminder
                    ? 'bg-[#536B53] text-white shadow-sm'
                    : 'text-gray-600 hover:text-gray-900',
                ]"
              >
                Ja
              </button>
              <button
                @click="reminder = false"
                :class="[
                  'text-xs md:text-sm font-semibold px-4 py-1.5 rounded-lg transition-all',
                  !reminder
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
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="currentColor"
            class="w-6 h-6 text-gray-900 md:w-7 md:h-7"
          >
            <path
              fill-rule="evenodd"
              d="M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25ZM12.75 6a.75.75 0 0 0-1.5 0v.75H9.75a.75.75 0 0 0 0 1.5h1.5v2.25H9a.75.75 0 0 0 0 1.5h2.25V15h-1.5a.75.75 0 0 0 0 1.5h1.5v1.5a.75.75 0 0 0 1.5 0v-1.5h1.5a.75.75 0 0 0 0-1.5h-1.5v-2.25H15a.75.75 0 0 0 0-1.5h-2.25V8.25h1.5a.75.75 0 0 0 0-1.5h-1.5V6Z"
              clip-rule="evenodd"
            />
          </svg>
          <h2 class="font-bold text-base md:text-lg lg:text-xl">App-Sprache</h2>
        </div>

        <div class="pl-9">
          <button
            @click="isDropdownOpen = !isDropdownOpen"
            class="flex items-center justify-between w-full max-w-[200px] md:max-w-[240px] border border-gray-200 rounded-xl px-3 py-2 bg-white hover:bg-gray-50 text-xs md:text-sm font-semibold shadow-sm transition-all text-left"
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

          <div
            v-if="isDropdownOpen"
            class="absolute left-9 mt-1 w-full max-w-[200px] md:max-w-[240px] bg-white border border-gray-200 rounded-xl shadow-xl z-50 overflow-hidden max-h-48 overflow-y-auto transition-all"
          >
            <button
              v-for="lang in availableLanguages"
              :key="lang.code"
              @click="selectLanguage(lang)"
              :class="[
                'w-full text-left px-4 py-2.5 text-xs md:text-sm font-medium transition-colors border-b border-gray-50 last:border-none',
                language === lang.code
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
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// Falls deine Variablen einzeln definiert sind, sieht dein Script jetzt so aus:
const fontSize = ref('normal')
const popups = ref(true)
const reminder = ref(true)
const language = ref('de')

const isDropdownOpen = ref(false)

const availableLanguages = [
  { code: 'de', label: 'Deutsch' },
  { code: 'en', label: 'English' },
]

const currentLanguageLabel = computed(() => {
  const found = availableLanguages.find((l) => l.code === language.value)
  return found ? found.label : 'Sprache wählen'
})

const selectLanguage = (lang) => {
  language.value = lang.code
  isDropdownOpen.value = false
}
</script>
