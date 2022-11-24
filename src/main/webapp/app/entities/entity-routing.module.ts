import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'equipe',
        data: { pageTitle: 'projetCommunalApp.equipe.home.title' },
        loadChildren: () => import('./equipe/equipe.module').then(m => m.EquipeModule),
      },
      {
        path: 'type-sport',
        data: { pageTitle: 'projetCommunalApp.typeSport.home.title' },
        loadChildren: () => import('./type-sport/type-sport.module').then(m => m.TypeSportModule),
      },
      {
        path: 'club',
        data: { pageTitle: 'projetCommunalApp.club.home.title' },
        loadChildren: () => import('./club/club.module').then(m => m.ClubModule),
      },
      {
        path: 'joueur',
        data: { pageTitle: 'projetCommunalApp.joueur.home.title' },
        loadChildren: () => import('./joueur/joueur.module').then(m => m.JoueurModule),
      },
      {
        path: 'combattant',
        data: { pageTitle: 'projetCommunalApp.combattant.home.title' },
        loadChildren: () => import('./combattant/combattant.module').then(m => m.CombattantModule),
      },
      {
        path: 'quartier',
        data: { pageTitle: 'projetCommunalApp.quartier.home.title' },
        loadChildren: () => import('./quartier/quartier.module').then(m => m.QuartierModule),
      },
      {
        path: 'archive-sport',
        data: { pageTitle: 'projetCommunalApp.archiveSport.home.title' },
        loadChildren: () => import('./archive-sport/archive-sport.module').then(m => m.ArchiveSportModule),
      },
      {
        path: 'match',
        data: { pageTitle: 'projetCommunalApp.match.home.title' },
        loadChildren: () => import('./match/match.module').then(m => m.MatchModule),
      },
      {
        path: 'competition',
        data: { pageTitle: 'projetCommunalApp.competition.home.title' },
        loadChildren: () => import('./competition/competition.module').then(m => m.CompetitionModule),
      },
      {
        path: 'vainqueur',
        data: { pageTitle: 'projetCommunalApp.vainqueur.home.title' },
        loadChildren: () => import('./vainqueur/vainqueur.module').then(m => m.VainqueurModule),
      },
      {
        path: 'don',
        data: { pageTitle: 'projetCommunalApp.don.home.title' },
        loadChildren: () => import('./don/don.module').then(m => m.DonModule),
      },
      {
        path: 'donneur',
        data: { pageTitle: 'projetCommunalApp.donneur.home.title' },
        loadChildren: () => import('./donneur/donneur.module').then(m => m.DonneurModule),
      },
      {
        path: 'beneficiaire',
        data: { pageTitle: 'projetCommunalApp.beneficiaire.home.title' },
        loadChildren: () => import('./beneficiaire/beneficiaire.module').then(m => m.BeneficiaireModule),
      },
      {
        path: 'annonce',
        data: { pageTitle: 'projetCommunalApp.annonce.home.title' },
        loadChildren: () => import('./annonce/annonce.module').then(m => m.AnnonceModule),
      },
      {
        path: 'vaccination',
        data: { pageTitle: 'projetCommunalApp.vaccination.home.title' },
        loadChildren: () => import('./vaccination/vaccination.module').then(m => m.VaccinationModule),
      },
      {
        path: 'type-vaccin',
        data: { pageTitle: 'projetCommunalApp.typeVaccin.home.title' },
        loadChildren: () => import('./type-vaccin/type-vaccin.module').then(m => m.TypeVaccinModule),
      },
      {
        path: 'cible',
        data: { pageTitle: 'projetCommunalApp.cible.home.title' },
        loadChildren: () => import('./cible/cible.module').then(m => m.CibleModule),
      },
      {
        path: 'don-sang',
        data: { pageTitle: 'projetCommunalApp.donSang.home.title' },
        loadChildren: () => import('./don-sang/don-sang.module').then(m => m.DonSangModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
