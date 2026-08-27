import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const firebaseConfig = {
  apiKey: "AIzaSyCnFrz9RyiojAuNfQRfYJRGQoDUEILjWBY",
  authDomain: "sattva-utsavam-dev.firebaseapp.com",
  projectId: "sattva-utsavam-dev",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Initialize Firebase Authentication and get a reference to the service
export const auth = getAuth(app);
