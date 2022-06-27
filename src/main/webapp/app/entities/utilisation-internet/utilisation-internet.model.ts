import { ILogiciel } from 'app/entities/logiciel/logiciel.model';
import { Profil } from 'app/entities/enumerations/profil.model';
import { Domaine } from 'app/entities/enumerations/domaine.model';

export interface IUtilisationInternet {
  id?: number;
  profil?: Profil | null;
  autre?: string | null;
  domaine?: Domaine | null;
  description?: string | null;
  logiciel?: ILogiciel | null;
}

export class UtilisationInternet implements IUtilisationInternet {
  constructor(
    public id?: number,
    public profil?: Profil | null,
    public autre?: string | null,
    public domaine?: Domaine | null,
    public description?: string | null,
    public logiciel?: ILogiciel | null
  ) {}
}

export function getUtilisationInternetIdentifier(utilisationInternet: IUtilisationInternet): number | undefined {
  return utilisationInternet.id;
}
