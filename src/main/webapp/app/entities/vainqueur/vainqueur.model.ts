import { ICompetition } from 'app/entities/competition/competition.model';
import { ICombattant } from 'app/entities/combattant/combattant.model';

export interface IVainqueur {
  id?: number;
  prix?: number | null;
  competitions?: ICompetition[] | null;
  combattants?: ICombattant[] | null;
}

export class Vainqueur implements IVainqueur {
  constructor(
    public id?: number,
    public prix?: number | null,
    public competitions?: ICompetition[] | null,
    public combattants?: ICombattant[] | null
  ) {}
}

export function getVainqueurIdentifier(vainqueur: IVainqueur): number | undefined {
  return vainqueur.id;
}
