import {apiFetch} from "./utils/http.ts";

export function getTowns() {
    return apiFetch<string[]>('/static-data/towns')
}

export function getKeywordSuggestions(query: string) {
    const params = new URLSearchParams()

    if (query.trim()) {
        params.set('query', query.trim())
    }

    const suffix = params.toString() ? `?${params.toString()}` : ''
    return apiFetch<string[]>(`/static-data/keywords${suffix}`)
}