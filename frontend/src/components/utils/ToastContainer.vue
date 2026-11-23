<template>
  <!-- pointer-events-none sorgt dafür, dass der leere Bereiche nichts unter sich blockiert -->
  <div
    class="fixed inset-x-0 z-50 flex flex-col items-center gap-2 px-4 pointer-events-none"
    style="top: calc(env(safe-area-inset-top, 0px) + 1rem)"
    aria-live="polite"
  >
    <TransitionGroup
      enter-active-class="transition duration-200"
      enter-from-class="opacity-0 -translate-y-2"
      leave-active-class="transition duration-200"
      leave-to-class="opacity-0"
    >
      <!-- pointer-events-auto lediglich die Toasts selbst reagieren auf z.B. Click -->
      <div
        v-for="t in toasts"
        :key="t.id"
        :role="t.art === 'fehler' ? 'alert' : 'status'"
        :class="[
          'pointer-events-auto w-full max-w-sm flex items-start gap-3 rounded-xl border px-4 py-3 shadow-md text-sm',
          farben[t.art],
        ]"
      >
        <span aria-hidden="true">{{ symbole[t.art] }}</span>
        <p class="flex-grow text-left">{{ t.text }}</p>
        <button
          @click="entferne(t.id)"
          class="opacity-60 hover:opacity-100"
          aria-label="Meldung schließen"
        >
          ✕
        </button>
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup>
import { useToast } from '@/composables/useToast.js'

const { toasts, entferne } = useToast()

const farben = {
  erfolg: 'bg-emerald-50 border-emerald-700 text-emerald-900',
  fehler: 'bg-red-50 border-red-700 text-red-900',
  info: 'bg-[#D3DDD1] border-gray-300 text-gray-900',
}
const symbole = { erfolg: '✓', fehler: '⚠', info: '🔔' }
</script>
