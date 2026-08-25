import fs from 'fs';
import path from 'path';

// Firebase project ID
const PROJECT_ID = 'sattva-utsavam-dev';
const BASE_URL = `https://${PROJECT_ID}.web.app`;

// Categories
const CATEGORIES = {
    pujas: [
        { id: "puja-1", prompt: "A highly cinematic, realistic, and premium wide shot of an ancient Indian temple during a grand Maha Rudrabhishek ceremony. Golden glowing lights, sacred fire (havan), smoke, marigold garlands, serene and divine atmosphere, 8k resolution, photorealistic" },
        { id: "puja-2", prompt: "A beautiful, premium, cinematic shot of Ganga Aarti at dusk in Varanasi. Priests holding large glowing brass oil lamps, river reflecting the warm golden light, crowds in the background, deep spiritual and calm atmosphere, high quality photography" }
    ],
    gaushalas: [
        { id: "gaushala-1", prompt: "A premium, photorealistic wide shot of a serene and well-maintained Indian cow sanctuary (Gaushala) at sunrise. Healthy native Indian cows resting peacefully. Greenery, traditional architecture, warm sunlight, calm and divine, 8k" },
        { id: "gaushala-2", prompt: "A cinematic and realistic photo of a traditional Indian Gaushala. Cows eating green fodder from clean troughs. Sunbeams shining through dust. Peaceful and well cared for. High quality documentary style" },
        { id: "gaushala-3", prompt: "A beautiful, premium wide shot of a modern, clean cow shelter in India. Cows relaxing under a large shed. Volunteers in the background. Bright daylight, happy animals, photorealistic" }
    ],
    animals: [
        { id: "animal-1", prompt: "A photorealistic, premium close-up portrait of a majestic Indian Nandi bull (cow) looking calm and peaceful. White coat, prominent hump, soft morning light, 8k resolution, beautiful details" },
        { id: "animal-2", prompt: "A realistic and heartwarming photo of a young Indian calf resting peacefully on soft hay. Warm sunlight, gentle eyes, premium documentary photography" },
        { id: "animal-3", prompt: "A highly realistic photo of an older, wise-looking Indian cow with long horns resting in a clean, sunny sanctuary. Peaceful, respectful, high quality" },
        { id: "animal-4", prompt: "A cinematic close up of a beautiful white Indian cow with a marigold garland around its neck. Soft, divine lighting, premium quality, photorealistic" },
        { id: "animal-5", prompt: "A realistic photo of a gentle cow being fed green grass by hand. Focus on the cow's calm face. Warm lighting, documentary style, 8k" }
    ]
};

async function downloadImage(prompt, filepath) {
    const encodedPrompt = encodeURIComponent(prompt);
    const url = `https://image.pollinations.ai/prompt/${encodedPrompt}?width=800&height=600&nologo=true`;
    
    console.log(`Downloading ${url} to ${filepath}`);
    const response = await fetch(url);
    if (!response.ok) throw new Error(`Failed to fetch image: ${response.statusText}`);
    
    const buffer = await response.arrayBuffer();
    fs.writeFileSync(filepath, Buffer.from(buffer));
}

async function fetchFirestore(collection) {
    const url = `https://firestore.googleapis.com/v1/projects/${PROJECT_ID}/databases/(default)/documents/${collection}?pageSize=100`;
    const res = await fetch(url);
    if (!res.ok) return [];
    const data = await res.json();
    return (data.documents || []).map(doc => {
        const item = { id: doc.name.split("/").pop(), name: doc.name };
        for (const [k, v] of Object.entries(doc.fields || {})) {
            if ("stringValue" in v) item[k] = v.stringValue;
        }
        return item;
    });
}

async function updateFirestoreDoc(docName, fields) {
    const encodedName = docName.split('/').map(encodeURIComponent).join('/');
    const url = `https://firestore.googleapis.com/v1/${encodedName}?updateMask.fieldPaths=imageUrl`;
    const payload = { fields: { imageUrl: { stringValue: fields.imageUrl } } };

    const res = await fetch(url, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });
    
    if (!res.ok) {
        console.error(`Failed to update ${docName}:`, await res.text());
    } else {
        console.log(`Updated ${docName} -> ${fields.imageUrl}`);
    }
}

async function main() {
    const baseDir = path.join(process.cwd(), 'assets');
    if (!fs.existsSync(baseDir)) fs.mkdirSync(baseDir);

    for (const [category, items] of Object.entries(CATEGORIES)) {
        const catDir = path.join(baseDir, category);
        if (!fs.existsSync(catDir)) fs.mkdirSync(catDir);

        for (const item of items) {
            const filename = `${item.id}.webp`;
            const filepath = path.join(catDir, filename);
            if (!fs.existsSync(filepath)) {
                await downloadImage(item.prompt, filepath);
            } else {
                console.log(`Exists: ${filepath}`);
            }
        }
    }

    console.log("Updating Firestore...");
    for (const category of ['pujas', 'gaushalas', 'animals']) {
        const docs = await fetchFirestore(category);
        const images = CATEGORIES[category];
        let idx = 0;

        for (const doc of docs) {
            if (images.length === 0) continue;
            const img = images[idx % images.length];
            const newUrl = `${BASE_URL}/${category}/${img.id}.webp`;
            
            if (doc.imageUrl !== newUrl) {
                await updateFirestoreDoc(doc.name, { imageUrl: newUrl });
            }
            idx++;
        }
    }
    console.log("Done!");
}

main().catch(console.error);
