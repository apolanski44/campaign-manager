import type { Campaign } from '../types/campaign'
import CampaignActions from './CampaignActions'

type Props = {
    campaigns: Campaign[]
    onEdit: (campaign: Campaign) => void
    onDelete: (id: string) => void
}

export default function CampaignTable({ campaigns, onEdit, onDelete }: Props) {
    return (
        <section className="card">
            <h2 className="balance-label">Campaigns</h2>

            {campaigns.length === 0 ? (
                <p>No campaigns yet.</p>
            ) : (
                <div className="table-wrapper">
                    <table className="campaign-table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Keywords</th>
                            <th>Bid</th>
                            <th>Fund</th>
                            <th>Status</th>
                            <th>Town</th>
                            <th>Radius</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {campaigns.map((campaign) => (
                            <tr key={campaign.id}>
                                <td>{campaign.campaignName}</td>
                                <td>{campaign.keywords.join(', ')}</td>
                                <td>{campaign.bidAmount}</td>
                                <td>{campaign.campaignFund}</td>
                                <td>{campaign.status}</td>
                                <td>{campaign.town}</td>
                                <td>{campaign.radius} km</td>
                                <td>
                                    <CampaignActions
                                        onEdit={() => onEdit(campaign)}
                                        onDelete={() => onDelete(campaign.id)}
                                    />
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </section>
    )
}