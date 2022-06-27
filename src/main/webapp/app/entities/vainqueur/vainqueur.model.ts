import { ICompetition } from 'app/entities/competition/competition.model';
import { ICombattant } from 'app/entities/combattant/combattant.model';

export interface IVainqueur {
  id?: number;
  prix?: number | null;
  competition?: ICompetition | null;
  combattant?: ICombattant | null;
}

export class Vainqueur implements IVainqueur {
  constructor(
    public id?: number,
    public prix?: number | null,
    public competition?: ICompetition | null,
    public combattant?: ICombattant | null
  ) {}
}

export function getVainqueurIdentifier(vainqueur: IVainqueur): number | undefined {
  return vainqueur.id;
}
