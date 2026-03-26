import KeywordTypeahead from './KeywordTypeahead'
import type {CampaignRequest} from "../types/campaign.ts";
import * as React from "react";
import PrimaryButton from "./PrimaryButton.tsx";

type Props = {
    form: CampaignRequest
    isEditing: boolean
    towns: string[]
    loading: boolean
    onChange: (
        e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
    ) => void
    onSubmit: (e: React.SubmitEvent) => void
    onCancelEdit: () => void
    onAddKeyword: (keyword: string) => void
    onRemoveKeyword: (keyword: string) => void
    fetchKeywordSuggestions: (query: string) => Promise<string[]>
}

export default function CampaignForm({
    form,
    isEditing,
    towns,
    loading,
    onChange,
    onSubmit,
    onCancelEdit,
    onAddKeyword,
    onRemoveKeyword,
    fetchKeywordSuggestions,
}: Props) {
    return (
        <section className="card">
            <div className="form-header">
                <h2 className="balance-label">{isEditing ? 'Edit campaign' : 'Create campaign'}</h2>

                {isEditing && (
                    <PrimaryButton type="button" onClick={onCancelEdit}>
                        Cancel
                    </PrimaryButton>
                )}
            </div>

            <form onSubmit={onSubmit} className="campaign-form">
                <div className="form-grid">
                    <div className="field">
                        <label>Campaign name</label>
                        <input
                            className="input"
                            name="campaignName"
                            value={form.campaignName}
                            onChange={onChange}
                            required
                        />
                    </div>

                    <div className="field">
                        <label>Status</label>
                        <select
                            className="input"
                            name="status"
                            value={form.status}
                            onChange={onChange}
                            required
                        >
                            <option value="ON">ON</option>
                            <option value="OFF">OFF</option>
                        </select>
                    </div>

                    <div className="field">
                        <label>Bid amount</label>
                        <input
                            className="input"
                            type="number"
                            name="bidAmount"
                            min="0.01"
                            step="0.01"
                            value={form.bidAmount}
                            onChange={onChange}
                            required
                        />
                    </div>

                    <div className="field">
                        <label>Campaign fund</label>
                        <input
                            className="input"
                            type="number"
                            name="campaignFund"
                            min="0.01"
                            step="0.01"
                            value={form.campaignFund}
                            onChange={onChange}
                            required
                        />
                    </div>

                    <div className="field">
                        <label>Town</label>
                        <select
                            className="input"
                            name="town"
                            value={form.town}
                            onChange={onChange}
                            required
                        >
                            <option value="">Select town</option>
                            {towns.map((town) => (
                                <option key={town} value={town}>
                                    {town}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="field">
                        <label>Radius (km)</label>
                        <input
                            className="input"
                            type="number"
                            name="radius"
                            min="1"
                            step="1"
                            value={form.radius}
                            onChange={onChange}
                            required
                        />
                    </div>
                </div>

                <KeywordTypeahead
                    selectedKeywords={form.keywords}
                    onAddKeyword={onAddKeyword}
                    onRemoveKeyword={onRemoveKeyword}
                    fetchSuggestions={fetchKeywordSuggestions}
                />

                <PrimaryButton type="submit" disabled={loading}>
                    {loading
                        ? 'Saving...'
                        : isEditing
                            ? 'Update campaign'
                            : 'Create campaign'}
                </PrimaryButton>
            </form>
        </section>
    )
}