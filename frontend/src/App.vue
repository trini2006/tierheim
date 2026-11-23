<template>
  <!-- Hier wird die jeweils aktive Route (z. B. dein AppLayout oder deine Testseite) geladen -->
  <RouterView />
  <ToastContainer />
</template>

<script setup>
  import { onMounted, onUnmounted } from 'vue'
  import ToastContainer from '@/components/utils/ToastContainer.vue'
  import { useBenachrichtigungenStore } from '@/stores/benachrichtigungen.js'

  const nachrichten = useBenachrichtigungenStore()

  const beiNachrichtVomServiceWorker = (e) => {
    if (e.data?.type === 'neue-nachricht') nachrichten.lade()
  }

  onMounted(() => {
    nachrichten.lade() // Grundstand laden, damit nur wirklich neue Nachrichten einen Toast auslösen
    navigator.serviceWorker?.addEventListener('message', beiNachrichtVomServiceWorker)
  })
  onUnmounted(() => {
    navigator.serviceWorker?.removeEventListener('message', beiNachrichtVomServiceWorker)
  })
</script>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
