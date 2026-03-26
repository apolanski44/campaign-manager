import PrimaryButton from "./PrimaryButton.tsx";

type Props = {
    onEdit: () => void
    onDelete: () => void
}

export default function CampaignActions({ onEdit, onDelete }: Props) {
    return (
        <div className="actions-row">
            <PrimaryButton type="button" onClick={onEdit}>
                Edit
            </PrimaryButton>

            <PrimaryButton type="button" onClick={onDelete}>
                Delete
            </PrimaryButton>
        </div>
    )
}