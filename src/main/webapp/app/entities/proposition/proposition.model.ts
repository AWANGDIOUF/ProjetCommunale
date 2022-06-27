import { IEnsegnant } from 'app/entities/ensegnant/ensegnant.model';

export interface IProposition {
  id?: number;
  description?: string | null;
  enseignant?: IEnsegnant | null;
}

export class Proposition implements IProposition {
  constructor(public id?: number, public description?: string | null, public enseignant?: IEnsegnant | null) {}
}

export function getPropositionIdentifier(proposition: IProposition): number | undefined {
  return proposition.id;
}
