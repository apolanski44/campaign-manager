import { useEffect, useState } from 'react'
import './App.css'
import AccountBalance from './components/AccountBalance'
import CampaignForm from './components/CampaignForm'
import CampaignTable from './components/CampaignTable'
import type { Campaign, CampaignRequest } from './types/campaign'
import {apiFetch} from "./api/utils/http.ts";
import * as React from "react";

const initialForm: CampaignRequest = {
  campaignName: '',
  keywords: [],
  bidAmount: '',
  campaignFund: '',
  status: 'ON',
  town: '',
  radius: 0,
}

export default function App() {
  const [campaigns, setCampaigns] = useState<Campaign[]>([])
  const [towns, setTowns] = useState<string[]>([])
  const [balance, setBalance] = useState('0.00')
  const [form, setForm] = useState<CampaignRequest>(initialForm)
  const [editingId, setEditingId] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const isEditing = editingId !== null

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    const [campaignsData, townsData, balanceData] = await Promise.all([
      apiFetch<Campaign[]>('/campaigns'),
      apiFetch<string[]>('/static-data/towns'),
      apiFetch<{ balance: string | number }>('/account/balance'),
    ])

    setCampaigns(campaignsData)
    setTowns(townsData)
    setBalance(String(balanceData.balance))
  }

  const fetchKeywordSuggestions = async (query: string) => {
    const suffix = query.trim()
        ? `/static-data/keywords?query=${encodeURIComponent(query)}`
        : '/static-data/keywords'

    return apiFetch<string[]>(suffix)
  }

  const handleInputChange = (
      e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target
    setForm((prev: any) => ({ ...prev, [name]: value }))
  }

  const handleAddKeyword = (keyword: string) => {
    setForm((prev: any) => ({
      ...prev,
      keywords: [...prev.keywords, keyword],
    }))
  }

  const handleRemoveKeyword = (keyword: string) => {
    setForm((prev: any) => ({
      ...prev,
      keywords: prev.keywords.filter((k: any) => k !== keyword),
    }))
  }

  const resetForm = () => {
    setForm(initialForm)
    setEditingId(null)
  }

  const handleEdit = (campaign: Campaign) => {
    setEditingId(campaign.id)
    setForm({
      campaignName: campaign.campaignName,
      keywords: [...campaign.keywords],
      bidAmount: String(campaign.bidAmount),
      campaignFund: String(campaign.campaignFund),
      status: campaign.status,
      town: campaign.town,
      radius: campaign.radius,
    })
  }

  const handleDelete = async (id: string) => {
    await apiFetch(`/campaigns/${id}`, { method: 'DELETE' })
    await loadData()

    if (editingId === id) {
      resetForm()
    }
  }

  const handleSubmit = async (e: React.SubmitEvent) => {
    e.preventDefault()
    setLoading(true)

    const payload = {
      campaignName: form.campaignName,
      keywords: form.keywords,
      bidAmount: form.bidAmount,
      campaignFund: form.campaignFund,
      status: form.status,
      town: form.town,
      radius: Number(form.radius),
    }

    try {
      if (isEditing && editingId) {
        await apiFetch(`/campaigns/${editingId}`, {
          method: 'PUT',
          body: JSON.stringify(payload),
        })
      } else {
        await apiFetch('/campaigns', {
          method: 'POST',
          body: JSON.stringify(payload),
        })
      }

      await loadData()
      resetForm()
    } finally {
      setLoading(false)
    }
  }

  return (
      <main className="page">
        <div className="container">
          <AccountBalance balance={balance} />

          <CampaignForm
              form={form}
              isEditing={isEditing}
              towns={towns}
              loading={loading}
              onChange={handleInputChange}
              onSubmit={handleSubmit}
              onCancelEdit={resetForm}
              onAddKeyword={handleAddKeyword}
              onRemoveKeyword={handleRemoveKeyword}
              fetchKeywordSuggestions={fetchKeywordSuggestions}
          />

          <CampaignTable
              campaigns={campaigns}
              onEdit={handleEdit}
              onDelete={handleDelete}
          />
        </div>
      </main>
  )
}
