const { Firestore } = require('@google-cloud/firestore');
const firestore = new Firestore({ projectId: 'sattva-utsavam-dev' });

async function seed() {
  const pujas = [
    {
      id: "maha_rudrabhishek",
      title: "Maha Rudrabhishek",
      specialTag: "Maha Shivratri Special",
      templeName: "Kashi Vishwanath Temple, Varanasi",
      location: "Kashi Vishwanath Temple, Varanasi",
      dateTimeStr: "Mar 8, 5:30 AM IST",
      durationStr: "2.5 Hours",
      devoteesCount: "12.5k Devotees attending",
      priceRupees: 2501,
      imageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuAOpqdNo42gJqDM8nBf0S-Hb22Q9tRGrWdjusk87abF3ywTcszYbUaaDwRjZ_O_q-L2aYmVY-Bl7IMXpzwqEvGnZrvr_f4OS3kecYgV_4HmNM5kAN2eXQHxpTzuQkJap3ol57kWh_-GN5ta8UgbQmOSYA96-mD2TrmVWP_V5LifI_9TKkIUiULJzAOIdNlzp0WzaSFwk-yWZdM0YCjvUY3f_9hnSGknRRk3soO_gznmp0zCUtITN40",
      significance: "The Maha Rudrabhishek is a highly potent Vedic ritual dedicated to Lord Shiva...",
      priestName: "Pt. Rameshwar Shastri",
      priestTitle: "Chief Priest, Kashi Vishwanath Mandir Trust",
      priestExp: "30+ Years Experience in Vedic Rituals",
      category: "Upcoming",
      isFeatured: true
    }
  ];

  const gaushalas = [
    {
      id: "shri_krishna_gaushala",
      name: "Shri Krishna Gaushala",
      location: "Vrindavan",
      state: "Uttar Pradesh",
      trustScorePercent: 98,
      animalsRescuedCount: 450,
      transparencyTier: "Gold Tier",
      imageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuB-rVXkCb9UZsqmd1VN9FZaoCFYEKr0UYrWaDyMz4G5cu0RqLrysF4jCD-jPZip07NznS7G4GYt5pMurcrhIP1Vn2VqtcMgctuUSfzpu0OAA-Ipdvk4r9D_XwkMJ2K5aTB-vzNLlPL27fnJi7NDA6l0tt9JgyHfOldp3b_fgayEo2iuSVb83FYpJkpPx7mpsuLChQSxvGKg3ruya1Ct_DI0pSA5A4pV9313MQnmkwstgP8clw8ThPQ",
      missionQuote: "Providing a lifelong, loving sanctuary...",
      fodderPercent: 65,
      medicalPercent: 40,
      shelterPercent: 85,
      lat: 27.58,
      lng: 77.70,
      updatesCount: 2
    }
  ];

  const animals = [
    {
      id: "nandi_01",
      gaushalaId: "shri_krishna_gaushala",
      name: "Nandi",
      ageStr: "3.5 Years",
      healthStatus: "Recovering",
      healthDescription: "Fractured left leg...",
      imageUrl: "https://lh3.googleusercontent.com/aida-public/AB6AXuA9kQ_J56K6b-67zQh6yM0GvJ7n8yC_8rT3pLmK6jN2v9bA4xZ7c8vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL6pQ1wN8zC9vM2kY4tL",
      story: "Rescued from a severe highway collision...",
      monthlyGoalRupees: 5000,
      raisedRupees: 3250,
      isUrgent: true
    }
  ];

  for(const p of pujas) await firestore.collection('pujas').doc(p.id).set(p);
  for(const g of gaushalas) await firestore.collection('gaushalas').doc(g.id).set(g);
  for(const a of animals) await firestore.collection('animals').doc(a.id).set(a);
  console.log('Seeded successfully!');
}
seed().catch(console.error);
