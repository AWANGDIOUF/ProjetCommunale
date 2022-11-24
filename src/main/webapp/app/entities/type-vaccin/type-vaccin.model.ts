import { IVaccination } from 'app/entities/vaccination/vaccination.model';

export interface ITypeVaccin {
  id?: number;
  libelle?: string | null;
  vaccination?: IVaccination | null;
}

export class TypeVaccin implements ITypeVaccin {
  constructor(public id?: number, public libelle?: string | null, public vaccination?: IVaccination | null) {}
}

export function getTypeVaccinIdentifier(typeVaccin: ITypeVaccin): number | undefined {
  return typeVaccin.id;
}
