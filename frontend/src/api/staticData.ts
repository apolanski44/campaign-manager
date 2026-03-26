import {apiFetch} from "./utils/http.ts";

export function getTowns() {
    return apiFetch<string[]>('/metadata/towns')
}

export function getKeywordSuggestions(query: string) {
    const params = new URLSearchParams()

    if (query.trim()) {
        params.set('query', query.trim())
    }

    const suffix = params.toString() ? `?${params.toString()}` : ''
    return apiFetch<string[]>(`/metadata/keywords${suffix}`)
}