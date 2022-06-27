import dayjs from 'dayjs/esm';

export interface IDomaineActivite {
  id?: number;
  typeActivite?: string | null;
  description?: string | null;
  dateActivite?: dayjs.Dayjs | null;
}

export class DomaineActivite implements IDomaineActivite {
  constructor(
    public id?: number,
    public typeActivite?: string | null,
    public description?: string | null,
    public dateActivite?: dayjs.Dayjs | null
  ) {}
}

export function getDomaineActiviteIdentifier(domaineActivite: IDomaineActivite): number | undefined {
  return domaineActivite.id;
}
