import type { ButtonHTMLAttributes, ReactNode } from 'react'

type Props = ButtonHTMLAttributes<HTMLButtonElement> & {
    children: ReactNode
}

export default function PrimaryButton({ children, ...props }: Props) {
    return (
        <button className="btn btn-primary" {...props}>
            {children}
        </button>
    )
}