import {apiFetch} from "./utils/http.ts";
import type {AccountBalance} from "../types/accountBalance.ts";

export function getAccountBalance() {
    return apiFetch<AccountBalance>('/account/balance')
}