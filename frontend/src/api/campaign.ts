import type { Campaign, CampaignRequest } from '../types/campaign'
import {apiFetch} from "./utils/http.ts";

export function getCampaigns() {
    return apiFetch<Campaign[]>('/campaigns')
}

export function getCampaignById(id: string) {
    return apiFetch<Campaign>(`/campaigns/${id}`)
}

export function createCampaign(payload: CampaignRequest) {
    return apiFetch<Campaign>('/campaigns', {
        method: 'POST',
        body: JSON.stringify(payload),
    })
}

export function updateCampaign(id: string, payload: CampaignRequest) {
    return apiFetch<Campaign>(`/campaigns/${id}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
    })
}

export function deleteCampaign(id: string) {
    return apiFetch<null>(`/campaigns/${id}`, {
        method: 'DELETE',
    })
}