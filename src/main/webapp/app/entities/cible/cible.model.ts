import { IVaccination } from 'app/entities/vaccination/vaccination.model';
import { CibleVacc } from 'app/entities/enumerations/cible-vacc.model';

export interface ICible {
  id?: number;
  cible?: CibleVacc | null;
  age?: number | null;
  vaccination?: IVaccination | null;
}

export class Cible implements ICible {
  constructor(public id?: number, public cible?: CibleVacc | null, public age?: number | null, public vaccination?: IVaccination | null) {}
}

export function getCibleIdentifier(cible: ICible): number | undefined {
  return cible.id;
}
