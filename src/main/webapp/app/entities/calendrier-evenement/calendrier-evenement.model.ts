import dayjs from 'dayjs/esm';

export interface ICalendrierEvenement {
  id?: number;
  nomEve?: string | null;
  but?: string | null;
  objectif?: string | null;
  date?: dayjs.Dayjs | null;
  lieu?: string | null;
}

export class CalendrierEvenement implements ICalendrierEvenement {
  constructor(
    public id?: number,
    public nomEve?: string | null,
    public but?: string | null,
    public objectif?: string | null,
    public date?: dayjs.Dayjs | null,
    public lieu?: string | null
  ) {}
}

export function getCalendrierEvenementIdentifier(calendrierEvenement: ICalendrierEvenement): number | undefined {
  return calendrierEvenement.id;
}
