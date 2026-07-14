<template>
  <div class="max-w-md mx-auto px-4 py-2">
    <!-- Header-Bereich: Alle als gelesen markieren -->
    <div class="flex justify-end items-center gap-2 mb-6">
      <button
        @click="markAllAsRead"
        class="text-xs font-semibold text-gray-700 hover:text-emerald-800 flex items-center gap-2 transition-colors"
      >
        <span>alle als gelesen markieren</span>
        <!-- Briefumschlag Icon -->
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          stroke-width="1.5"
          stroke="currentColor"
          class="w-6 h-6"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M21.75 6.75v10.5a2.25 2.25 0 0 1-2.25 2.25h-15a2.25 2.25 0 0 1-2.25-2.25V6.75m19.5 0A2.25 2.25 0 0 0 19.5 4.5h-15a2.25 2.25 0 0 0-2.25 2.25m19.5 0v.243a2.25 2.25 0 0 1-1.07 1.916l-7.5 4.615a2.25 2.25 0 0 1-2.36 0L3.32 8.91a2.25 2.25 0 0 1-1.07-1.916V6.75"
          />
        </svg>
      </button>
    </div>

    <!-- Nachrichten-Liste -->
    <div class="space-y-4">
      <div
        v-for="msg in notifications"
        :key="msg.id"
        :class="[
          'flex items-center justify-between p-4 rounded-[2rem] border transition-all shadow-sm',
          msg.read ? 'bg-[#D3DDD1]/40 border-gray-200 opacity-60' : 'bg-[#D3DDD1] border-gray-300',
        ]"
      >
        <!-- Nachrichtentext (Rot bei wichtigen Warnungen, sonst Dunkelgrau) -->
        <div class="text-left pr-4">
          <p
            :class="[
              'text-xs font-semibold leading-relaxed',
              msg.urgent && !msg.read ? 'text-red-600' : 'text-gray-800',
            ]"
          >
            {{ msg.text }}
          </p>
        </div>

        <!-- Haken-Button zum einzelnen Gelesen-Markieren -->
        <button @click="toggleRead(msg.id)" class="flex-shrink-0 focus:outline-none">
          <div
            :class="[
              'w-7 h-7 rounded-full border-2 flex items-center justify-center transition-colors',
              msg.read
                ? 'border-gray-500 text-gray-500 bg-transparent'
                : 'border-gray-900 text-gray-900 bg-white/20 hover:bg-white/40',
            ]"
          >
            <!-- Checkmark / Haken SVG -->
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="3"
              stroke="currentColor"
              class="w-4 h-4"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5" />
            </svg>
          </div>
        </button>
      </div>
    </div>

    <!-- Fallback, wenn keine Benachrichtigungen da sind -->
    <div v-if="notifications.length === 0" class="text-center py-12 text-gray-400 italic">
      Keine neuen Nachrichten vorhanden.
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Reaktiver State für die Nachrichten aus deinem Screenshot
const notifications = ref([
  {
    id: 1,
    text: 'Hund Akito wurde gesperrt! Die Reservierung vom 05.07. wurde aufgehoben.',
    urgent: true,
    read: false,
  },
  {
    id: 2,
    text: 'Ihre Reservierung vom 03.04. wurde storniert! Grund: Tier adoptiert',
    urgent: false,
    read: false,
  },
])

// Einzelne Nachricht als gelesen markieren / umschalten
const toggleRead = (id) => {
  const item = notifications.value.find((n) => n.id === id)
  if (item) {
    item.read = !item.read
  }
}

// Alle auf einmal als gelesen markieren
const markAllAsRead = () => {
  notifications.value.forEach((n) => (n.read = true))
}
</script>
