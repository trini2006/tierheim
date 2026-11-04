export const formatiereDatumKurz = (s) => {
  if (!s) return ''
  const [jahr, monat, tag] = s.split('-')
  return jahr && monat && tag ? `${tag}.${monat}.${jahr}` : s
}

export const formatiereDatumLang = (s) => {
  if (!s) return ''
  const [jahr, monat, tag] = s.split('-')
  if (!jahr || !monat || !tag) return s
  const wochentag = new Date(jahr, monat - 1, tag).toLocaleDateString('de-DE', { weekday: 'long' })
  return `${wochentag}, ${tag}.${monat}.${jahr}`
}

export const formatiereUhrzeit = (s) => (s ? s.substring(0, 5) : '')
