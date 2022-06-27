import { IArtiste } from 'app/entities/artiste/artiste.model';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { IClub } from 'app/entities/club/club.model';
import { IBeneficiaire } from 'app/entities/beneficiaire/beneficiaire.model';
import { ICollecteurOdeur } from 'app/entities/collecteur-odeur/collecteur-odeur.model';
import { IVidange } from 'app/entities/vidange/vidange.model';
import { IRecuperationRecyclable } from 'app/entities/recuperation-recyclable/recuperation-recyclable.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { IEleveur } from 'app/entities/eleveur/eleveur.model';

export interface IQuartier {
  id?: number;
  nomQuartier?: string | null;
  artiste?: IArtiste | null;
  equipes?: IEquipe[] | null;
  clubs?: IClub[] | null;
  beneficiaires?: IBeneficiaire[] | null;
  collecteurOdeurs?: ICollecteurOdeur[] | null;
  vidanges?: IVidange[] | null;
  recuperationRecyclables?: IRecuperationRecyclable[] | null;
  etablissements?: IEtablissement[] | null;
  eleveurs?: IEleveur[] | null;
}

export class Quartier implements IQuartier {
  constructor(
    public id?: number,
    public nomQuartier?: string | null,
    public artiste?: IArtiste | null,
    public equipes?: IEquipe[] | null,
    public clubs?: IClub[] | null,
    public beneficiaires?: IBeneficiaire[] | null,
    public collecteurOdeurs?: ICollecteurOdeur[] | null,
    public vidanges?: IVidange[] | null,
    public recuperationRecyclables?: IRecuperationRecyclable[] | null,
    public etablissements?: IEtablissement[] | null,
    public eleveurs?: IEleveur[] | null
  ) {}
}

export function getQuartierIdentifier(quartier: IQuartier): number | undefined {
  return quartier.id;
}
