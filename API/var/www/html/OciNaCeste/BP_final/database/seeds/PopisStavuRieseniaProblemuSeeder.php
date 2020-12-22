<?php

use App\Models\PopisStavuRieseniaProblemu;
use Illuminate\Database\Seeder;

class PopisStavuRieseniaProblemuSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $popisy = [
            [
                'popis' => 'Nepriradený popis',
                'problem_id' => 1

            ],

        ];

        foreach ($popisy as $popis_stavu_riesenia_poblemu) {
            PopisStavuRieseniaProblemu::create(array(
                'popis' => $popis_stavu_riesenia_poblemu['popis'],
                'problem_id' => $popis_stavu_riesenia_poblemu['problem_id']
            ));
        }
    }
}
