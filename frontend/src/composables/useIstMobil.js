import { ref, onMounted, onUnmounted } from 'vue'

const QUERY = '(max-width: 767px)' // = unterhalb von Tailwind "md"

// einmalige Abfrage, z. B. für den Router
export const istMobilJetzt = () => window.matchMedia(QUERY).matches

// reaktiv, reagiert auf Fenstergröße/Drehen des Geräts
export function useIstMobil() {
  const mq = window.matchMedia(QUERY)
  const istMobil = ref(mq.matches)
  const update = (e) => {
    istMobil.value = e.matches
  }
  onMounted(() => mq.addEventListener('change', update))
  onUnmounted(() => mq.removeEventListener('change', update))
  return istMobil
}
