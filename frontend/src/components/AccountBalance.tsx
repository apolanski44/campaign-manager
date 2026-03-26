import type {AccountBalance} from "../types/accountBalance.ts";

export default function AccountBalance({ balance }: AccountBalance) {
    return (
        <section className="card">
            <p className="balance-label">Emerald account balance</p>
            <h2 className="balance-value">{balance}</h2>
        </section>
    )
}