<template>
  <div class="w-full min-h-screen bg-white px-6 py-8 text-gray-800">
    <div class="w-full max-w-xl mx-auto space-y-10">
      <!-- Sektion: Persönliches (nur sichtbar, wenn die vorherige Seite KEIN Admin-Pfad war) -->
      <section>
        <div class="flex items-center justify-between mb-2">
          <div class="flex items-center gap-3">
            <span class="text-2xl">👤</span>
            <h2 class="text-xl font-bold">Persönliches</h2>
          </div>
          <div
            v-if="erfahrung && rolle === 'user'"
            :class="[
              'w-4 h-4 rounded-full mt-1 flex-shrink-0',
              erfahrung === 'true' ? 'bg-orange-500' : 'bg-green-500',
            ]"
          />
        </div>
        <div class="px-2">
          <p class="text-gray-600 text-sm">
            <span v-if="rolle === 'admin'"> Personalnummer:</span>
            <span v-else-if="rolle === 'user'">Mitgliedsnummer:</span>
            {{ username }}
          </p>
        </div>
      </section>

      <!-- Sektion 1: Allgemein
      <section>
        <div class="flex items-center gap-3 mb-6">
          <span class="text-2xl">🔧</span>
          <h2 class="text-xl font-bold">Allgemein</h2>
        </div>
        <div class="flex items-center justify-between px-2">
          <span class="font-medium text-gray-800">Schriftgröße</span>
          <div class="bg-gray-100 p-1 rounded-full flex gap-1">
            <button @click="fontSize = 'normal'" :class="[fontSize === 'normal' ? 'bg-[#536B53] text-white' : 'hover:bg-gray-200']" class="px-5 py-1.5 rounded-full text-sm font-medium transition">Normal</button>
            <button @click="fontSize = 'groß'" :class="[fontSize === 'groß' ? 'bg-[#536B53] text-white' : 'hover:bg-gray-200']" class="px-5 py-1.5 rounded-full text-sm font-medium transition">Groß</button>
          </div>
        </div>
      </section>
      -->

      <!-- Sektion 2: Benachrichtigungen -->
      <section>
        <div class="flex items-center gap-3 mb-6">
          <span class="text-2xl">🔔</span>
          <h2 class="text-xl font-bold">Benachrichtigungen</h2>
        </div>
        <div class="px-2 space-y-3">
          <div class="flex items-center justify-between gap-4">
            <div>
              <span class="text-gray-800 block">Push-Nachrichten</span>
              <span class="text-xs text-gray-500">
                Meldung aufs Gerät, wenn ein Termin storniert wird.
              </span>
            </div>
            <div
              v-if="status === 'an' || status === 'aus'"
              class="bg-gray-100 p-1 rounded-full flex gap-1 flex-shrink-0"
            >
              <button
                @click="schalte(true)"
                :disabled="laeuft"
                :class="[status === 'an' ? 'bg-[#536B53] text-white' : 'hover:bg-gray-200']"
                class="px-5 py-1.5 rounded-full text-sm font-medium transition"
              >
                An
              </button>
              <button
                @click="schalte(false)"
                :disabled="laeuft"
                :class="[status === 'aus' ? 'bg-[#536B53] text-white' : 'hover:bg-gray-200']"
                class="px-5 py-1.5 rounded-full text-sm font-medium transition"
              >
                Aus
              </button>
            </div>
          </div>

          <p v-if="status === 'ios-installieren'" class="text-sm text-gray-600">
            Auf dem iPhone und iPad geht das nur, wenn die App auf dem Home-Bildschirm liegt: Im
            Safari auf „Teilen“ tippen, „Zum Home-Bildschirm“ wählen und die App von dort öffnen.
          </p>
          <p v-else-if="status === 'nicht-unterstuetzt'" class="text-sm text-gray-600">
            Dieser Browser unterstützt keine Push-Nachrichten. Neue Nachrichten findest du in der
            App unter „Nachrichten“.
          </p>
          <p v-else-if="status === 'blockiert'" class="text-sm text-gray-600">
            Benachrichtigungen sind in diesem Browser blockiert. Du kannst sie in den
            Seiteneinstellungen (Schloss-Symbol neben der Adresse) wieder erlauben.
          </p>
          <p v-if="fehler" class="text-sm text-red-700">{{ fehler }}</p>
        </div>
      </section>

      <!-- Sektion 3: App-Sprache -->
      <section>
        <div class="flex items-center gap-3 mb-6">
          <span class="text-2xl">🌐</span>
          <h2 class="text-xl font-bold">App-Sprache</h2>
        </div>
        <div class="px-2">
          <p class="text-gray-700">Deutsch</p>
        </div>
      </section>

      <!-- Trennlinie -->
      <hr class="border-gray-200 my-6" />

      <!-- Sektion 4: Abmelden / Logout -->
      <section class="px-2">
        <button
          @click="ausloggen"
          class="w-full py-3 px-4 bg-red-50 hover:bg-red-100 text-red-700 font-semibold rounded-2xl border border-red-200 transition-colors flex items-center justify-center gap-2"
        >
          <span>🚪</span> Abmelden
        </button>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { usePush } from '@/composables/usePush.js'
import { useToast } from '@/composables/useToast.js'
import { useRouter } from 'vue-router'
import { useReservierungenStore } from '@/stores/reservierungen.js'
import { useBenachrichtigungenStore } from '@/stores/benachrichtigungen.js'

const toast = useToast()
const { status, laeuft, fehler, pruefeStatus, aktivieren, deaktivieren, beimAbmelden } = usePush()

onMounted(pruefeStatus)

const schalte = async (an) => {
  if (an === (status.value === 'an')) return
  const ok = an ? await aktivieren() : await deaktivieren()
  if (ok)
    toast.erfolg(
      an ? 'Push-Nachrichten sind eingeschaltet.' : 'Push-Nachrichten sind ausgeschaltet.',
    )
}

const router = useRouter()
const erfahrung = localStorage.getItem('erfahrung')
const username = localStorage.getItem('userName')
const rolle = localStorage.getItem('userRole')

const fontSize = ref('normal')
const sound = ref(true)
const reminder = ref(true)

const ausloggen = async () => {
  await beimAbmelden()
  // Löscht die gespeicherte Rolle und leitet zum Login zurück
  useReservierungenStore().reset()
  useBenachrichtigungenStore().reset()
  localStorage.removeItem('userId')
  localStorage.removeItem('erfahrung')
  localStorage.removeItem('userName')
  localStorage.removeItem('userRole')
  router.push('/')
}
</script>
