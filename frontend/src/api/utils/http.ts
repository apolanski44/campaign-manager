export async function apiFetch<T>(
    path: string,
    options?: RequestInit
): Promise<T> {
    const response = await fetch(`${import.meta.env.VITE_API_URL}${path}`, {
        headers: {
            'Content-Type': 'application/json',
            ...(options?.headers ?? {}),
        },
        ...options,
    })

    if (!response.ok) {
        let message = `Request failed with status ${response.status}`

        try {
            const errorBody = await response.json()
            if (errorBody?.message) {
                message = errorBody.message
            }
        } catch {
        }

        throw new Error(message)
    }

    if (response.status === 204) {
        return null as T
    }

    return await response.json() as Promise<T>
}