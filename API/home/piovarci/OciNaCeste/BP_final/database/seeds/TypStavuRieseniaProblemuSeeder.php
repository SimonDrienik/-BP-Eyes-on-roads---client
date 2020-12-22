<?php

use App\Models\TypStavuRieseniaProblemu;
use Illuminate\Database\Seeder;

class TypStavuRieseniaProblemuSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $typy = [
            [
                'nazov' => 'prijate',
            ],
            [
                'nazov' => 'potvrdene',
            ],
            [
                'nazov' => 'v procese',
            ],
            [
                'nazov' => 'ukoncene',
            ],
            [
                'nazov' => 'odlozene'
            ]
        ];

        foreach ($typy as $typ_stavu_riesenia_problemu) {
            TypStavuRieseniaProblemu::create(array(
                'nazov' => $typ_stavu_riesenia_problemu['nazov']
            ));
        }
    }
}
