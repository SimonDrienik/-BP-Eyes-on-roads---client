<?php

use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     *
     * @return void
     */
    public function run()
    {


                $this->call(SpravcaSeeder::class);
                $this->call(ObecSeeder::class);
                $this->call(KatastralneUzemieSeeder::class);
                $this->call(KrajSeeder::class);
                $this->call(KategoriaProblemuSeeder::class);
                $this->call(StavProblemuSeeder::class);
                $this->call(CestaSeeder::class);
                //$this->call(UsekSeeder::class);
                $this->call(TypStavuRieseniaProblemuSeeder::class);
                $this->call(VozidloSeeder::class);
                $this->call(RolaSeeder::class);
                $this->call(PrioritaSeeder::class);
                $this->call(UserSeeder::class);


                //$this->call(ProblemSeeder::class);
                //$this->call(PriradenyZamestnanecSeeder::class);
                //$this->call(PopisStavuRieseniaProblemuSeeder::class);
                //$this->call(FotkaStavuRieseniaProblemuSeeder::class);
                //$this->call(FotkaProblemuSeeder::class);
                //$this->call(KomentarSeeder::class);
                //$this->call(StavRieseniaProblemuSeeder::class);
                //$this->call(PriradeneVozidloSeeder::class);

    }
}
