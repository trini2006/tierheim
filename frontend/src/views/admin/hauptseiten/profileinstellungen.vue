<template>
  <div class="profile-settings-view">
    <div class="profile-container">
      
      <aside class="avatar-column">
        <div class="circle-avatar-placeholder">
          </div>
        <button class="edit-btn">Bild bearbeiten</button>
      </aside>

      <main class="data-column">
        
        <section class="section-title-group">
          <div class="heading-with-edit">
            <h2>Tierheim Weiden</h2>
            <button class="edit-icon-btn" aria-label="Name bearbeiten">✏️</button>
          </div>
          <div class="color-row">
            <span>Leitfarbe</span>
            <span class="color-dot"></span>
          </div>
        </section>

        <div class="details-grid">
          
          <div class="grid-block">
            <div class="heading-with-edit">
              <h3>Kontaktdaten</h3>
              <button class="edit-icon-btn" aria-label="Kontaktdaten bearbeiten">✏️</button>
            </div>
            <div class="block-body">
              <p>Schustermooslohe 96</p>
              <p>92637 Weiden i. d. Oberpfalz</p>
              <p>0961 25780</p>
              <p>0176 47159671 (Notfall)</p>
              <p class="link-text">tierheim-weiden.de</p>
            </div>
          </div>

          <div class="grid-block">
            <div class="heading-with-edit">
              <h3>Kalenderdaten</h3>
              <button class="edit-icon-btn" aria-label="Kalenderdaten bearbeiten">✏️</button>
            </div>
            <div class="block-body">
              <div class="timeblock-row">
                <span>Zeitblock:</span>
                <div class="chip-group">
                  <span class="chip active">30</span>
                  <span class="chip">45</span>
                  <span class="chip">60</span>
                </div>
              </div>
              <p>Standard Ruhezeiten (Hund): 60</p>
              <p>max. Gassi: 120</p>
              <p>min. Gassi: 30</p>
            </div>
          </div>

          <div class="grid-block">
            <div class="heading-with-edit">
              <h3>Gassi Zeiten</h3>
              <button class="edit-icon-btn" aria-label="Gassi Zeiten bearbeiten">✏️</button>
            </div>
            <div class="block-body">
              <div class="schedule-table">
                <div class="schedule-row">
                  <span class="day-col">Mo</span>
                  <span class="time-col">10:00 - 14:00 Uhr</span>
                </div>
                <div class="schedule-row">
                  <span class="day-col">Mi</span>
                  <span class="time-col">10:00 - 14:00 Uhr</span>
                </div>
                <div class="schedule-row">
                  <span class="day-col">Fr</span>
                  <span class="time-col">10:00 - 14:00 Uhr</span>
                </div>
                <div class="schedule-row">
                  <span class="day-col">Sa</span>
                  <span class="time-col">10:00 - 11:30 Uhr</span>
                </div>
              </div>
            </div>
          </div>

        </div>
      </main>

    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
/* Hauptbereich ohne extra Grauton */
.profile-settings-view {
  width: 100%;
  padding: 40px 60px;
  background-color: #ffffff;
  box-sizing: border-box;
}

.profile-container {
  display: flex;
  gap: 60px;
  max-width: 1100px;
  margin: 0 auto;
}

/* Linke Spalte */
.avatar-column {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 180px;
}

.circle-avatar-placeholder {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  border: 1px solid #ccc;
  margin-bottom: 20px;
}

.edit-btn {
  background-color: #dcdcdc;
  border: none;
  border-radius: 4px;
  padding: 8px 18px;
  font-size: 14px;
  color: #333;
  cursor: pointer;
}

.edit-btn:hover {
  background-color: #d0d0d0;
}

/* Rechte Spalte */
.data-column {
  flex: 1;
}

.heading-with-edit {
  display: flex;
  align-items: center;
  gap: 12px;
}

.heading-with-edit h2 {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
  color: #000;
}

.heading-with-edit h3 {
  font-size: 20px;
  font-weight: bold;
  margin: 0;
  color: #000;
}

.edit-icon-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  padding: 0;
}

.color-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 6px;
  margin-bottom: 35px;
  color: #333;
  font-size: 15px;
}

.color-dot {
  width: 16px;
  height: 16px;
  background-color: #8B2626;
  border-radius: 50%;
  display: inline-block;
}

/* Grid Layout */
.details-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  column-gap: 60px;
  row-gap: 35px;
}

.block-body {
  margin-top: 10px;
  font-size: 15px;
  color: #222;
  line-height: 1.5;
}

.block-body p {
  margin: 3px 0;
}

.link-text {
  color: #000;
  text-decoration: underline;
  cursor: pointer;
}

/* Zeitblock & Chips */
.timeblock-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.chip-group {
  display: flex;
  gap: 6px;
}

.chip {
  background-color: #e8e8e8;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
}

.chip.active {
  background-color: #798b7a;
  color: #ffffff;
}

/* Gassi Liste */
.schedule-table {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.schedule-row {
  display: flex;
  gap: 24px;
}

.day-col {
  font-weight: bold;
  width: 25px;
}

.time-col {
  color: #222;
}
</style>