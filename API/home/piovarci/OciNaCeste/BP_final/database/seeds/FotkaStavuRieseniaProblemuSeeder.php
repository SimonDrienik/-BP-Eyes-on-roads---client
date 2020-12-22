<?php

use App\Models\FotkaStavuRieseniaProblemu;
use Illuminate\Database\Seeder;

class FotkaStavuRieseniaProblemuSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $fotky = [
            [
                'popis_stavu_riesenia_id' => 1,
                'cesta_k_suboru' => 'nepriradena fotka',
            ],
        ];

        foreach ($fotky as $fotka_stavu_riesenia_problemu) {
            FotkaStavuRieseniaProblemu::create(array(
                'popis_stavu_riesenia_id' => $fotka_stavu_riesenia_problemu['popis_stavu_riesenia_id'],
                'cesta_k_suboru' => $fotka_stavu_riesenia_problemu['cesta_k_suboru'],
            ));
        }
    }
}
