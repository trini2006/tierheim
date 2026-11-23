const TIERHEIM_ID = 1 // ToDo: aus dem Login ableiten

// 'tierheim/1' | 'mitglied/5' | null (nicht eingeloggt)
export const leseEmpfaengerPfad = () => {
  const istAdmin = (localStorage.getItem('userRole') ?? '').toLowerCase() === 'admin'
  if (istAdmin) return `tierheim/${TIERHEIM_ID}`
  let id = null
  try {
    id = JSON.parse(localStorage.getItem('userId'))
  } catch {
    /* nicht eingeloggt */
  }
  return id ? `mitglied/${id}` : null
}
