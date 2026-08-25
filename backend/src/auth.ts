export async function verifyAuthToken(request: Request, apiKey: string): Promise<string | null> {
  const authHeader = request.headers.get("Authorization");
  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    return null;
  }
  const token = authHeader.substring(7);
  
  try {
    const url = `https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=${apiKey}`;
    const resp = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ idToken: token }),
    });
    
    if (!resp.ok) {
      return null;
    }
    
    const data: any = await resp.json();
    if (data.users && data.users.length > 0 && data.users[0].localId) {
      return data.users[0].localId;
    }
  } catch (err) {
    return null;
  }
  
  return null;
}
