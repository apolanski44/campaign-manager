import { useEffect, useState } from 'react'

type Props = {
    selectedKeywords: string[]
    onAddKeyword: (keyword: string) => void
    onRemoveKeyword: (keyword: string) => void
    fetchSuggestions: (query: string) => Promise<string[]>
}

export default function KeywordTypeahead({
                                             selectedKeywords,
                                             onAddKeyword,
                                             onRemoveKeyword,
                                             fetchSuggestions,
                                         }: Props) {
    const [query, setQuery] = useState('')
    const [suggestions, setSuggestions] = useState<string[]>([])

    useEffect(() => {
        let cancelled = false

        const load = async () => {
            const data = await fetchSuggestions(query)

            if (!cancelled) {
                setSuggestions(
                    data.filter((keyword) => !selectedKeywords.includes(keyword))
                )
            }
        }

        load()

        return () => {
            cancelled = true
        }
    }, [query, selectedKeywords, fetchSuggestions])

    const handleAdd = (keyword: string) => {
        onAddKeyword(keyword)
        setQuery('')
    }

    return (
        <div className="field">
            <label>Keywords</label>

            <input
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Type keyword..."
                className="input"
            />

            {suggestions.length > 0 && (
                <div className="suggestions">
                    {suggestions.map((keyword) => (
                        <button
                            key={keyword}
                            type="button"
                            className="suggestion-btn"
                            onClick={() => handleAdd(keyword)}
                        >
                            {keyword}
                        </button>
                    ))}
                </div>
            )}

            <div className="chips">
                {selectedKeywords.map((keyword) => (
                    <span key={keyword} className="chip">
            {keyword}
                        <button
                            type="button"
                            className="chip-remove"
                            onClick={() => onRemoveKeyword(keyword)}
                        >
              ×
            </button>
          </span>
                ))}
            </div>
        </div>
    )
}