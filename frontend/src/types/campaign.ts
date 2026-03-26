export type CampaignStatus = 'ON' | 'OFF'

export interface Campaign {
    id: string
    campaignName: string
    keywords: string[]
    bidAmount: string
    campaignFund: string
    status: CampaignStatus
    town: string
    radius: number
}

export interface CampaignRequest {
    campaignName: string
    keywords: string[]
    bidAmount: string
    campaignFund: string
    status: CampaignStatus
    town: string
    radius: number
}