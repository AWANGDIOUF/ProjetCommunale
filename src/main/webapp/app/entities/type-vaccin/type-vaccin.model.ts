import dayjs from 'dayjs/esm';

export interface ITypeVaccin {
  id?: number;
  libelle?: string | null;
  objectif?: dayjs.Dayjs | null;
}

export class TypeVaccin implements ITypeVaccin {
  constructor(public id?: number, public libelle?: string | null, public objectif?: dayjs.Dayjs | null) {}
}

export function getTypeVaccinIdentifier(typeVaccin: ITypeVaccin): number | undefined {
  return typeVaccin.id;
}
