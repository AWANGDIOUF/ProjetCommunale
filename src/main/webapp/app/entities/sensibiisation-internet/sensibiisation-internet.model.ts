import dayjs from 'dayjs/esm';

export interface ISensibiisationInternet {
  id?: number;
  theme?: dayjs.Dayjs | null;
  interdiction?: string | null;
  bonnePratique?: string | null;
}

export class SensibiisationInternet implements ISensibiisationInternet {
  constructor(
    public id?: number,
    public theme?: dayjs.Dayjs | null,
    public interdiction?: string | null,
    public bonnePratique?: string | null
  ) {}
}

export function getSensibiisationInternetIdentifier(sensibiisationInternet: ISensibiisationInternet): number | undefined {
  return sensibiisationInternet.id;
}
